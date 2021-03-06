package fr.gouv.education.foad.filebrowser.portlet.service;

import fr.gouv.education.foad.filebrowser.portlet.configuration.FileBrowserConfiguration;
import fr.gouv.education.foad.filebrowser.portlet.model.*;
import fr.gouv.education.foad.filebrowser.portlet.model.comparator.FileBrowserItemComparator;
import fr.gouv.education.foad.filebrowser.portlet.repository.FileBrowserRepository;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.liveedit.OnlyofficeLiveEditHelper;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;
import org.jboss.portal.common.invocation.Scope;
import org.jboss.portal.core.controller.ControllerContext;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.cms.impl.BasicPermissions;
import org.osivia.portal.api.cms.impl.BasicPublicationInfos;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.html.AccessibilityRoles;
import org.osivia.portal.api.html.DOM4JUtils;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.PortalUrlType;
import org.osivia.portal.api.user.UserPreferences;
import org.osivia.portal.core.cms.CMSBinaryContent;
import org.osivia.portal.core.constants.InternalConstants;
import org.osivia.portal.core.context.ControllerContextAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.portlet.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * File browser portlet service implementation.
 *
 * @author Cédric Krommenhoek
 * @see FileBrowserService
 */
@Service
public class FileBrowserServiceImpl implements FileBrowserService {

    /** Sort criteria attribute. */
    private static final String SORT_CRITERIA_ATTRIBUTE = "foad.file-browser.criteria";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private FileBrowserRepository repository;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;

    /** Document DAO. */
    @Autowired
    private DocumentDAO documentDao;


    /**
     * Constructor.
     */
    public FileBrowserServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public FileBrowserView getView(PortalControllerContext portalControllerContext, String viewId) throws PortletException {
        // View
        FileBrowserView view;
        
        if (StringUtils.isEmpty(viewId)) {
            // Current document
            NuxeoDocumentContext documentContext = this.repository.getCurrentDocumentContext(portalControllerContext);
            Document document = documentContext.getDoc();
            // WebId
            String webId = document.getString("ttc:webid");

            // User preferences
            UserPreferences userPreferences = this.repository.getUserPreferences(portalControllerContext);
            
            if(userPreferences != null) {
            // 	Saved view
            	String savedView = userPreferences.getFolderDisplayMode(webId);
                view = FileBrowserView.fromId(savedView);
            }
            else {
            	view = FileBrowserView.DEFAULT;
            }
            
        } else {
            view = FileBrowserView.fromId(viewId);
        }
        
        return view;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveView(PortalControllerContext portalControllerContext, FileBrowserView view) throws PortletException {
        // Current document
        NuxeoDocumentContext documentContext = this.repository.getCurrentDocumentContext(portalControllerContext);
        Document document = documentContext.getDoc();
        // WebId
        String webId = document.getString("ttc:webid");

        // User preferences
        UserPreferences userPreferences = this.repository.getUserPreferences(portalControllerContext);

        if(userPreferences != null) {
        	userPreferences.updateFolderDisplayMode(webId, view.getId());
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public FileBrowserForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Form
        FileBrowserForm form = this.applicationContext.getBean(FileBrowserForm.class);

        if (!form.isInitialized()) {
            // Current document context
            NuxeoDocumentContext documentContext = this.repository.getCurrentDocumentContext(portalControllerContext);

            // Documents
            List<Document> documents = this.repository.getDocuments(portalControllerContext);

            // User subscriptions
            Set<String> userSubscriptions = this.repository.getUserSubscriptions(portalControllerContext);

            // Items
            List<FileBrowserItem> items;
            if (CollectionUtils.isEmpty(documents)) {
                items = null;
            } else {
                items = new ArrayList<>(documents.size());

                for (Document document : documents) {
                    FileBrowserItem item = this.createItem(portalControllerContext, document);

                    // Subscription
                    boolean subscription = userSubscriptions.contains(document.getId());
                    item.setSubscription(subscription);

                    items.add(item);
                }
            }
            form.setItems(items);

            // Sort
            this.sortItems(portalControllerContext, form, FileBrowserSort.TITLE, false);

            // Uploadable indicator
            boolean uploadable = (documentContext.getType() != null) && CollectionUtils.isNotEmpty(documentContext.getType().getPortalFormSubTypes());
            form.setUploadable(uploadable);

            // Max file size
            form.setMaxFileSize(FileBrowserConfiguration.MAX_UPLOAD_SIZE_PER_FILE);

            // Initialized indicator
            form.setInitialized(true);
        }

        return form;
    }


    /**
     * Create file browser item.
     *
     * @param portalControllerContext portal controller context
     * @param nuxeoDocument Nuxeo document
     * @return item
     * @throws PortletException
     */
    private FileBrowserItem createItem(PortalControllerContext portalControllerContext, Document nuxeoDocument) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Item
        FileBrowserItem item = this.applicationContext.getBean(FileBrowserItem.class);

        // Document DTO
        DocumentDTO documentDto = this.documentDao.toDTO(nuxeoDocument);
        item.setDocument(documentDto);
        // Document type
        DocumentType type = documentDto.getType();

        // Title
        String title = documentDto.getTitle();
        item.setTitle(title);

        // Lock icon
        String lockOwner = nuxeoDocument.getString("ttc:lockOwner");
        String lockIcon;
        if (StringUtils.isEmpty(lockOwner)) {
            lockIcon = null;
        } else if (StringUtils.equals(request.getRemoteUser(), lockOwner)) {
            lockIcon = "glyphicons glyphicons-user-lock";
        } else {
            lockIcon = "glyphicons glyphicons-lock";
        }
        item.setLock(lockIcon);

        // Last modification
        Date lastModification = nuxeoDocument.getDate("dc:modified");
        item.setLastModification(lastModification);

        // Last contributor
        String lastContributor = nuxeoDocument.getString("dc:lastContributor");
        item.setLastContributor(lastContributor);

        // File size
        Long size = nuxeoDocument.getLong("common:size");
        item.setSize(size);

        // Folderish indicator
        boolean folderish = ((type != null) && type.isFolderish());
        item.setFolderish(folderish);

        // File MIME type
        if ((type != null) && type.isFile()) {
            PropertyMap fileContent = nuxeoDocument.getProperties().getMap("file:content");
            if (fileContent != null) {
                String mimeType = fileContent.getString("mime-type");
                item.setMimeType(mimeType);
            }
        }

        // Folderish accepted types
        if (folderish) {
            List<String> acceptedTypes = type.getPortalFormSubTypes();
            item.setAcceptedTypes(StringUtils.join(acceptedTypes, ","));
        }

        return item;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void sortItems(PortalControllerContext portalControllerContext, FileBrowserForm form, FileBrowserSort sort, boolean alt) throws PortletException {
        // Controller context
        ControllerContext controllerContext = ControllerContextAdapter.getControllerContext(portalControllerContext);


        // Sort criteria
        FileBrowserSortCriteria criteria;

        if (form.isInitialized()) {
            criteria = this.applicationContext.getBean(FileBrowserSortCriteria.class);
            criteria.setSort(sort);
            criteria.setAlt(alt);

            controllerContext.setAttribute(Scope.PRINCIPAL_SCOPE, SORT_CRITERIA_ATTRIBUTE, criteria);
        } else {
            Object criteriaAttribute = controllerContext.getAttribute(Scope.PRINCIPAL_SCOPE, SORT_CRITERIA_ATTRIBUTE);

            if ((criteriaAttribute == null) || !(criteriaAttribute instanceof FileBrowserSortCriteria)) {
                criteria = this.applicationContext.getBean(FileBrowserSortCriteria.class);
                criteria.setSort(sort);
                criteria.setAlt(alt);
            } else {
                criteria = (FileBrowserSortCriteria) criteriaAttribute;
            }
        }


        if (CollectionUtils.isNotEmpty(form.getItems())) {
            // Comparator
            FileBrowserItemComparator comparator = this.applicationContext.getBean(FileBrowserItemComparator.class, criteria);

            Collections.sort(form.getItems(), comparator);
        }

        // Update model
        form.setCriteria(criteria);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Element getToolbar(PortalControllerContext portalControllerContext, List<String> indexes, String viewId) throws PortletException {
        // Toolbar container
        Element container = DOM4JUtils.generateDivElement(null);

        // Toolbar
        Element toolbar = DOM4JUtils.generateDivElement("btn-toolbar", AccessibilityRoles.TOOLBAR);
        container.add(toolbar);

        if (CollectionUtils.isNotEmpty(indexes)) {
            // Internationalization bundle
            Bundle bundle = this.bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());

            // Form
            FileBrowserForm form = this.getForm(portalControllerContext);

            // File browser items
            List<FileBrowserItem> items = form.getItems();

            // Selected file browser items
            List<FileBrowserItem> selectedItems = new ArrayList<>(indexes.size());
            for (String index : indexes) {
                int i = NumberUtils.toInt(index, -1);
                if ((i > -1) && (i < items.size())) {
                    FileBrowserItem item = items.get(i);
                    selectedItems.add(item);
                }
            }

            if (indexes.size() == selectedItems.size()) {
                // All editable indicator
                boolean allEditable = true;
                // All file indicator
                boolean allFile = true;

                // View
                FileBrowserView view = this.getView(portalControllerContext, viewId);

                // Selected documents
                List<DocumentDTO> selection = new ArrayList<>(selectedItems.size());

                Iterator<FileBrowserItem> iterator = selectedItems.iterator();
                while (iterator.hasNext() && (allEditable || allFile)) {
                    // Selected file browser item
                    FileBrowserItem item = iterator.next();
                    // Document DTO
                    DocumentDTO documentDto = item.getDocument();
                    selection.add(documentDto);
                    // Document type
                    DocumentType type = documentDto.getType();

                    if ((type != null) && type.isSupportsPortalForms()) {
                        // Permissions
                        BasicPermissions permissions = item.getPermissions();
                        if (permissions == null) {
                            permissions = this.repository.getPermissions(portalControllerContext, documentDto.getDocument());
                            item.setPermissions(permissions);
                        }

                        allEditable = allEditable && permissions.isEditableByUser();
                    } else {
                        allEditable = false;
                    }

                    allFile = allFile && (type != null) && type.isFile();
                }


                if (indexes.size() == 1) {
                    // Selected file browser item
                    FileBrowserItem item = selectedItems.get(0);

                    // Document DTO
                    DocumentDTO documentDto = item.getDocument();

                    // Permissions
                    BasicPermissions permissions = item.getPermissions();
                    if (permissions == null) {
                        permissions = this.repository.getPermissions(portalControllerContext, documentDto.getDocument());
                        item.setPermissions(permissions);
                    }


                    // Live edition
                    Element liveEditionGroup = this.getToolbarLiveEditionGroup(portalControllerContext, documentDto, permissions, bundle);
                    toolbar.add(liveEditionGroup);


                    // Single selection
                    Element singleSelectionGroup = this.getToolbarSingleSelectionGroup(portalControllerContext, view, documentDto, permissions, bundle);
                    toolbar.add(singleSelectionGroup);
                } else {
                    // Bulk download
                    Element bulkDownload;
                    if (allFile) {
                        String title = bundle.getString("FILE_BROWSER_TOOLBAR_DOWNLOAD");
                        String url = this.getBulkDownloadUrl(portalControllerContext, selection);
                        bulkDownload = DOM4JUtils.generateLinkElement(url, null, null, "btn btn-default btn-sm no-ajax-link", null,
                                "glyphicons glyphicons-download-alt");
                        DOM4JUtils.addAttribute(bulkDownload, "title", title);
                    } else {
                        bulkDownload = DOM4JUtils.generateLinkElement("#", null, null, "btn btn-default btn-sm disabled", null,
                                "glyphicons glyphicons-download-alt");
                    }
                    toolbar.add(bulkDownload);
                }

                // Multiple selection
                Element multipleSelectionGroup = this.getToolbarMultipleSelectionGroup(portalControllerContext, view, selection, allEditable, container,
                        bundle);
                toolbar.add(multipleSelectionGroup);
            }
        }

        return container;
    }


    /**
     * Get toolbar live edition group DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param documentDto document DTO
     * @param permissions permissions
     * @param bundle internationalization bundle
     * @return DOM element
     * @throws PortletException
     */
    private Element getToolbarLiveEditionGroup(PortalControllerContext portalControllerContext, DocumentDTO documentDto, BasicPermissions permissions,
            Bundle bundle) throws PortletException {
        // Portlet request
        PortletRequest portletRequest = portalControllerContext.getRequest();

        // Document path
        String path = documentDto.getPath();
        // Document type
        DocumentType documentType = documentDto.getType();

        // Publication infos
        BasicPublicationInfos publicationInfos;
        // Nuxeo drive indicator
        boolean drive;
        if (permissions.isEditableByUser() && (documentType != null) && documentType.isFile() && documentType.isLiveEditable()) {
            publicationInfos = this.repository.getPublicationInfos(portalControllerContext, documentDto.getDocument());
            drive = StringUtils.isNotEmpty(publicationInfos.getDriveUrl()) || publicationInfos.isDriveEnabled();
        } else {
            publicationInfos = null;
            drive = false;
        }


        // Live edition group
        Element liveEditionGroup = DOM4JUtils.generateDivElement("btn-group btn-group-sm hidden-xs");
        // Dropdown
        Element dropdownGroup;
        Element dropdownMenu;
        if (permissions.isEditableByUser() && (documentDto.isLiveEditable() || drive)) {
            // Dropdown group
            dropdownGroup = DOM4JUtils.generateDivElement("btn-group btn-group-sm");

            // Dropdown button
            Element dropdownButton = DOM4JUtils.generateElement("button", "btn btn-default dropdown-toggle", null);
            DOM4JUtils.addAttribute(dropdownButton, "type", "button");
            DOM4JUtils.addDataAttribute(dropdownButton, "toggle", "dropdown");
            dropdownGroup.add(dropdownButton);

            // Dropdown button caret
            Element dropdownButtonCaret = DOM4JUtils.generateElement("span", "caret", StringUtils.EMPTY);
            dropdownButton.add(dropdownButtonCaret);

            // Dropdown menu
            dropdownMenu = DOM4JUtils.generateElement("ul", "dropdown-menu dropdown-menu-right", null);
            dropdownGroup.add(dropdownMenu);
        } else {
            dropdownGroup = null;
            dropdownMenu = null;
        }

        if (documentDto.isLiveEditable()) {
            if (permissions.isEditableByUser()) {
                String onlyOfficeTitle = bundle.getString("FILE_BROWSER_TOOLBAR_ONLYOFFICE_TITLE");
                String onlyOfficeWithLockText = bundle.getString("FILE_BROWSER_TOOLBAR_ONLYOFFICE_WITH_LOCK");
                String onlyOfficeWithoutLockText = bundle.getString("FILE_BROWSER_TOOLBAR_ONLYOFFICE_WITHOUT_LOCK");

                String onlyOfficeWithLockUrl = this.getOnlyOfficeUrl(portalControllerContext, path, onlyOfficeTitle, true, true);
                String onlyOfficeWithoutLockUrl = this.getOnlyOfficeUrl(portalControllerContext, path, onlyOfficeWithoutLockText, true, false);


                // OnlyOffice (with lock)
                Element onlyOffice = DOM4JUtils.generateLinkElement(onlyOfficeWithLockUrl, null, null, "btn btn-default no-ajax-link", onlyOfficeTitle,
                        "glyphicons glyphicons-pencil");
                liveEditionGroup.add(onlyOffice);

                // OnlyOffice (with lock)
                Element onlyOfficeWithLockDropdownItem = DOM4JUtils.generateElement("li", null, null);
                dropdownMenu.add(onlyOfficeWithLockDropdownItem);
                Element onlyOfficeWithLockLink = DOM4JUtils.generateLinkElement(onlyOfficeWithLockUrl, null, null, "no-ajax-link", onlyOfficeWithLockText);
                onlyOfficeWithLockDropdownItem.add(onlyOfficeWithLockLink);

                // OnlyOffice (without lock)
                Element onlyOfficeWithoutLockDropdownItem = DOM4JUtils.generateElement("li", null, null);
                dropdownMenu.add(onlyOfficeWithoutLockDropdownItem);
                Element onlyOfficeWithoutLockLink = DOM4JUtils.generateLinkElement(onlyOfficeWithoutLockUrl, null, null, "no-ajax-link",
                        onlyOfficeWithoutLockText);
                onlyOfficeWithoutLockDropdownItem.add(onlyOfficeWithoutLockLink);
            }

            if (StringUtils.isNotEmpty(portletRequest.getRemoteUser())) {
                // OnlyOffice (read only)
                String onlyOfficeReadOnlyTitle = bundle.getString("FILE_BROWSER_TOOLBAR_ONLYOFFICE_READ_ONLY_TITLE");
                String onlyOfficeReadOnlyText = bundle.getString("FILE_BROWSER_TOOLBAR_ONLYOFFICE_READ_ONLY");
                String onlyOfficeReadOnlyUrl = this.getOnlyOfficeUrl(portalControllerContext, path, onlyOfficeReadOnlyTitle, false, false);

                if (permissions.isEditableByUser()) {
                    Element onlyOfficeReadOnlyDropdownItem = DOM4JUtils.generateElement("li", null, null);
                    dropdownMenu.add(onlyOfficeReadOnlyDropdownItem);
                    Element onlyOfficeReadOnlyLink = DOM4JUtils.generateLinkElement(onlyOfficeReadOnlyUrl, null, null, "no-ajax-link", onlyOfficeReadOnlyText);
                    onlyOfficeReadOnlyDropdownItem.add(onlyOfficeReadOnlyLink);
                } else {
                    Element onlyOfficeReadOnly = DOM4JUtils.generateLinkElement(onlyOfficeReadOnlyUrl, null, null, "btn btn-default no-ajax-link",
                            onlyOfficeReadOnlyText);
                    liveEditionGroup.add(onlyOfficeReadOnly);
                }
            }
        }

        if (drive) {
            if (documentDto.isLiveEditable()) {
                // Divider
                Element divider = DOM4JUtils.generateElement("li", "divider", StringUtils.EMPTY);
                dropdownMenu.add(divider);
            }

            if (StringUtils.isNotEmpty(publicationInfos.getDriveUrl())) {
                // Drive
                Element driveDropdownItem = DOM4JUtils.generateElement("li", null, null);
                dropdownMenu.add(driveDropdownItem);
                Element driveLink = DOM4JUtils.generateLinkElement(publicationInfos.getDriveUrl(), null, null, "no-ajax-link",
                        bundle.getString("FILE_BROWSER_TOOLBAR_DRIVE_EDIT"));
                driveDropdownItem.add(driveLink);
            } else {
                // Drive not started warning
                Element warning = DOM4JUtils.generateElement("li", "dropdown-header", bundle.getString("FILE_BROWSER_TOOLBAR_DRIVE_NOT_STARTED_WARNING"));
                dropdownMenu.add(warning);

                // Drive
                Element driveDropdownItem = DOM4JUtils.generateElement("li", "disabled", null);
                dropdownMenu.add(driveDropdownItem);
                Element driveLink = DOM4JUtils.generateLinkElement("#", null, null, null, bundle.getString("FILE_BROWSER_TOOLBAR_DRIVE_EDIT"));
                driveDropdownItem.add(driveLink);
            }
        }


        if (dropdownGroup != null) {
            liveEditionGroup.add(dropdownGroup);
        }

        return liveEditionGroup;
    }


    /**
     * Get single selection group DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param view view
     * @param documentDto document DTO
     * @param permissions permissions
     * @param bundle internationalization bundle
     * @return DOM element
     * @throws PortletException
     */
    private Element getToolbarSingleSelectionGroup(PortalControllerContext portalControllerContext, FileBrowserView view, DocumentDTO documentDto,
            BasicPermissions permissions, Bundle bundle) throws PortletException {
        // Nuxeo document
        Document nuxeoDocument = documentDto.getDocument();
        // Document path
        String path = documentDto.getPath();


        // Single selection group
        Element group = DOM4JUtils.generateDivElement("btn-group btn-group-sm");

        // Rename
        Element rename;
        if (permissions.isEditableByUser()) {
            String title = bundle.getString("FILE_BROWSER_TOOLBAR_RENAME");
            String url = this.getRenameUrl(portalControllerContext, path);
            rename = DOM4JUtils.generateLinkElement("javascript:;", null, null, "btn btn-default no-ajax-link", null, "glyphicons glyphicons-edit");
            DOM4JUtils.addAttribute(rename, "title", title);
            DOM4JUtils.addDataAttribute(rename, "target", "#osivia-modal");
            DOM4JUtils.addDataAttribute(rename, "load-url", url);
        } else {
            rename = DOM4JUtils.generateLinkElement("#", null, null, "btn btn-default disabled", null, "glyphicons glyphicons-edit");
        }
        group.add(rename);

        // Download
        Element download;
        if ((documentDto.getType() != null) && documentDto.getType().isFile()) {
            String title = bundle.getString("FILE_BROWSER_TOOLBAR_DOWNLOAD");
            String url = this.repository.getDownloadUrl(portalControllerContext, nuxeoDocument);
            download = DOM4JUtils.generateLinkElement(url, "_blank", null, "btn btn-default no-ajax-link", null, "glyphicons glyphicons-download-alt");
            DOM4JUtils.addAttribute(download, "title", title);
        } else {
            download = DOM4JUtils.generateLinkElement("#", null, null, "btn btn-default disabled", null, "glyphicons glyphicons-download-alt");
        }
        group.add(download);

        // Duplicate
        Element duplicate;
        if (permissions.isCopiable()) {
            String title = bundle.getString("FILE_BROWSER_TOOLBAR_DUPLICATE");
            String url = this.getDuplicateUrl(portalControllerContext, path, view);
            duplicate = DOM4JUtils.generateLinkElement(url, null, null, "btn btn-default", null, "glyphicons glyphicons-duplicate");
            DOM4JUtils.addAttribute(duplicate, "title", title);
        } else {
            duplicate = DOM4JUtils.generateLinkElement("#", null, null, "btn btn-default disabled", null, "glyphicons glyphicons-duplicate");
        }
        group.add(duplicate);

        return group;
    }


    /**
     * Get toolbar multiple selection group DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param view view
     * @param selection selected documents
     * @param allEditable all editable indicator
     * @param container toolbar container DOM element
     * @param bundle internationalization bundle
     * @return DOM element
     * @throws PortletException
     */
    private Element getToolbarMultipleSelectionGroup(PortalControllerContext portalControllerContext, FileBrowserView view, List<DocumentDTO> selection,
            boolean allEditable, Element container, Bundle bundle) throws PortletException {
        // Namespace
        String namespace = portalControllerContext.getResponse().getNamespace();

        // Delete modal identifier
        String deleteId = namespace + "-delete";

        // Selected identifiers
        List<String> identifiers;
        // Selected paths
        List<String> paths;
        // Selected accepted types
        Set<String> acceptedTypes;

        // Unknown document type indicator
        boolean unknownType = false;

        if (allEditable) {
            identifiers = new ArrayList<>(selection.size());
            paths = new ArrayList<>(selection.size());
            acceptedTypes = new HashSet<>();

            for (DocumentDTO documentDto : selection) {
                // Identifier
                String identifier = documentDto.getId();
                identifiers.add(identifier);

                // Path
                String path = documentDto.getPath();
                paths.add(path);

                // Accepted types
                String type;
                if (documentDto.getType() == null) {
                    type = null;
                } else {
                    type = documentDto.getType().getName();
                }
                if (StringUtils.isEmpty(type)) {
                    unknownType = true;
                } else {
                    acceptedTypes.add(type);
                }
            }
        } else {
            identifiers = null;
            paths = null;
            acceptedTypes = null;
        }


        // Group
        Element group = DOM4JUtils.generateDivElement("btn-group btn-group-sm");


        // Move
        Element move;
        if (allEditable && !unknownType) {
            String title = bundle.getString("FILE_BROWSER_TOOLBAR_MOVE");
            String url = this.getMoveUrl(portalControllerContext, identifiers, paths, acceptedTypes);
            move = DOM4JUtils.generateLinkElement(url, null, null, "btn btn-default fancyframe_refresh no-ajax-link", null, "glyphicons glyphicons-move");
            DOM4JUtils.addAttribute(move, "title", title);
        } else {
            move = DOM4JUtils.generateLinkElement("#", null, null, "btn btn-default disabled", null, "glyphicons glyphicons-move");
        }
        group.add(move);


        // Delete
        Element delete;
        if (allEditable) {
            String title = bundle.getString("FILE_BROWSER_TOOLBAR_DELETE");
            delete = DOM4JUtils.generateLinkElement("#" + deleteId, null, null, "btn btn-default no-ajax-link", null, "glyphicons glyphicons-bin");
            DOM4JUtils.addAttribute(delete, "title", title);
            DOM4JUtils.addDataAttribute(delete, "toggle", "modal");

            // Delete confirmation modal
            Element modal = this.getToolbarDeleteModal(portalControllerContext, identifiers, view, deleteId, bundle);
            container.add(modal);
        } else {
            delete = DOM4JUtils.generateLinkElement("#", null, null, "btn btn-default disabled", null, "glyphicons glyphicons-bin");
        }
        group.add(delete);

        return group;
    }


    /**
     * Get OnlyOffice URL.
     *
     * @param portalControllerContext portal controller context
     * @param path document path
     * @param title title
     * @param edit edit mode indicator
     * @param lock lock indicator
     * @return URL
     * @throws PortletException
     */
    private String getOnlyOfficeUrl(PortalControllerContext portalControllerContext, String path, String title, boolean edit, boolean lock) throws PortletException {
        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put(Constants.WINDOW_PROP_URI, path);
        properties.put("osivia.hideTitle", String.valueOf(1));
        properties.put("osivia.onlyoffice.mode", BooleanUtils.toString(edit, "edit", "view"));
        properties.put("osivia.onlyoffice.withLock", String.valueOf(lock));
        properties.put(InternalConstants.PROP_WINDOW_TITLE, title);

        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, OnlyofficeLiveEditHelper.ONLYOFFICE_PORTLET_INSTANCE, properties);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
    }


    /**
     * Get rename URL.
     *
     * @param portalControllerContext portal controller context
     * @param path document path
     * @return URL
     * @throws PortletException
     */
    private String getRenameUrl(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put(Constants.WINDOW_PROP_URI, path);
        properties.put("osivia.rename.document.redirect.path", this.repository.getCurrentPath(portalControllerContext));

        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "toutatice-portail-cms-nuxeo-rename-portlet-instance", properties,
                    PortalUrlType.MODAL);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
    }


    /**
     * Get duplicate URL.
     *
     * @param portalControllerContext portal controller context
     * @param path document path
     * @param view view
     * @return URL
     * @throws PortletException
     */
    private String getDuplicateUrl(PortalControllerContext portalControllerContext, String path, FileBrowserView view) throws PortletException {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();

        // URL
        String url;
        if (portletResponse instanceof MimeResponse) {
            MimeResponse mimeResponse = (MimeResponse) portletResponse;

            // Action URL
            PortletURL actionUrl = mimeResponse.createActionURL();
            actionUrl.setParameter(ActionRequest.ACTION_NAME, "duplicate");
            actionUrl.setParameter("path", path);
            actionUrl.setParameter("view", view.getId());

            url = actionUrl.toString();
        } else {
            url = "#";
        }

        return url;
    }


    /**
     * Get bulk download URL.
     * 
     * @param portalControllerContext portal controller context
     * @param selection selected documents
     * @return URL
     */
    private String getBulkDownloadUrl(PortalControllerContext portalControllerContext, List<DocumentDTO> selection) {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();

        // URL
        String url;
        if (portletResponse instanceof MimeResponse) {
            MimeResponse mimeResponse = (MimeResponse) portletResponse;

            // Paths
            String[] paths = new String[selection.size()];
            int i = 0;
            for (DocumentDTO documentDto : selection) {
                paths[i] = documentDto.getPath();
                i++;
            }

            // Resource URL
            ResourceURL resourceUrl = mimeResponse.createResourceURL();
            resourceUrl.setResourceID("bulk-download");
            resourceUrl.setParameter("paths", paths);

            url = resourceUrl.toString();
        } else {
            url = "#";
        }

        return url;
    }


    /**
     * Get move URL.
     *
     * @param portalControllerContext portal controller context
     * @param identifiers selected document identifiers
     * @param paths selected document paths
     * @param acceptedTypes selected document types
     * @return URL
     * @throws PortletException
     */
    private String getMoveUrl(PortalControllerContext portalControllerContext, List<String> identifiers, List<String> paths, Set<String> acceptedTypes)
            throws PortletException {
        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put("osivia.move.documentPath", this.repository.getCurrentPath(portalControllerContext));
        properties.put("osivia.move.documentsIdentifiers", StringUtils.join(identifiers, ","));
        properties.put("osivia.move.ignoredPaths", StringUtils.join(paths, ","));
        properties.put("osivia.move.cmsBasePath", this.repository.getBasePath(portalControllerContext));
        properties.put("osivia.move.acceptedTypes", StringUtils.join(acceptedTypes, ","));

        // URL
        String url;
        try {
            url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, "toutatice-portail-cms-nuxeo-move-portlet-instance", properties,
                    PortalUrlType.POPUP);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        return url;
    }


    /**
     * Get delete URL.
     *
     * @param portalControllerContext portal controller context
     * @param identifiers selected document identifiers
     * @param view view
     * @return URL
     * @throws PortletException
     */
    private String getDeleteUrl(PortalControllerContext portalControllerContext, List<String> identifiers, FileBrowserView view) throws PortletException {
        // Portlet response
        PortletResponse portletResponse = portalControllerContext.getResponse();

        // URL
        String url;
        if (portletResponse instanceof MimeResponse) {
            MimeResponse mimeResponse = (MimeResponse) portletResponse;

            // Action URL
            PortletURL actionUrl = mimeResponse.createActionURL();
            actionUrl.setParameter(ActionRequest.ACTION_NAME, "delete");
            actionUrl.setParameter("identifiers", StringUtils.join(identifiers, ","));
            actionUrl.setParameter("view", view.getId());

            url = actionUrl.toString();
        } else {
            url = "#";
        }

        return url;
    }


    /**
     * Get toolbar delete modal confirmation DOM element.
     *
     * @param portalControllerContext portal controller context
     * @param identifiers selected document identifiers
     * @param view view
     * @param id modal identifier
     * @param bundle internationalization bundle
     * @return DOM element
     * @throws PortletException
     */
    private Element getToolbarDeleteModal(PortalControllerContext portalControllerContext, List<String> identifiers, FileBrowserView view, String id,
            Bundle bundle) throws PortletException {
        // Modal
        Element modal = DOM4JUtils.generateDivElement("modal fade");
        DOM4JUtils.addAttribute(modal, "id", id);

        // Modal dialog
        Element modalDialog = DOM4JUtils.generateDivElement("modal-dialog");
        modal.add(modalDialog);

        // Modal content
        Element modalContent = DOM4JUtils.generateDivElement("modal-content");
        modalDialog.add(modalContent);

        // Modal header
        Element modalHeader = DOM4JUtils.generateDivElement("modal-header");
        modalContent.add(modalHeader);

        // Modal close button
        Element close = DOM4JUtils.generateElement("button", "close", null, "glyphicons glyphicons-remove", null);
        DOM4JUtils.addAttribute(close, "type", "button");
        DOM4JUtils.addDataAttribute(close, "dismiss", "modal");
        modalHeader.add(close);

        // Modal title
        Element modalTitle = DOM4JUtils.generateElement("h4", "modal-title", bundle.getString("FILE_BROWSER_TOOLBAR_DELETE_MODAL_TITLE"));
        modalHeader.add(modalTitle);

        // Modal body
        Element modalBody = DOM4JUtils.generateDivElement("modal-body");
        modalContent.add(modalBody);

        // Modal message
        Element message = DOM4JUtils.generateElement("p", null, bundle.getString("FILE_BROWSER_TOOLBAR_DELETE_MODAL_MESSAGE"));
        modalBody.add(message);

        // Modal footer
        Element modalFooter = DOM4JUtils.generateDivElement("modal-footer");
        modalContent.add(modalFooter);

        // Confirmation button
        String url = this.getDeleteUrl(portalControllerContext, identifiers, view);
        Element confirm = DOM4JUtils.generateLinkElement(url, null, null, "btn btn-warning no-ajax-link", bundle.getString("FILE_BROWSER_TOOLBAR_DELETE"),
                "glyphicons glyphicons-bin");
        modalFooter.add(confirm);

        // Cancel button
        Element cancel = DOM4JUtils.generateElement("button", "btn btn-default", bundle.getString("CANCEL"), "glyphicons glyphicons-remove", null);
        DOM4JUtils.addAttribute(cancel, "type", "button");
        DOM4JUtils.addDataAttribute(cancel, "dismiss", "modal");
        modalFooter.add(cancel);

        return modal;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void duplicate(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        try {
            // Duplicate
            this.repository.duplicate(portalControllerContext, path);

            // Notification
            String message = bundle.getString("FILE_BROWSER_DUPLICATE_SUCCESS_MESSAGE");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        } catch (NuxeoException e) {
            // Notification
            String message = bundle.getString("FILE_BROWSER_DUPLICATE_ERROR_MESSAGE");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.ERROR);
        }

        // Refresh navigation
        request.setAttribute(Constants.PORTLET_ATTR_UPDATE_CONTENTS, Constants.PORTLET_VALUE_ACTIVATE);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(PortalControllerContext portalControllerContext, List<String> identifiers) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        try {
            // Delete
            this.repository.delete(portalControllerContext, identifiers);

            // Notification
            String message = bundle.getString("FILE_BROWSER_DELETE_SUCCESS_MESSAGE");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        } catch (NuxeoException e) {
            // Notification
            String message = bundle.getString("FILE_BROWSER_DELETE_ERROR_MESSAGE");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.ERROR);
        }

        // Refresh navigation
        request.setAttribute(Constants.PORTLET_ATTR_UPDATE_CONTENTS, Constants.PORTLET_VALUE_ACTIVATE);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public FileBrowserBulkDownloadContent getBulkDownload(PortalControllerContext portalControllerContext, List<String> paths)
            throws PortletException, IOException {
        // Binary content
        CMSBinaryContent binaryContent = this.repository.getBinaryContent(portalControllerContext, paths);

        // Bulk download content
        FileBrowserBulkDownloadContent content = this.applicationContext.getBean(FileBrowserBulkDownloadContent.class);

        // Content type
        String mimeType = binaryContent.getMimeType();
        content.setType(mimeType);

        // Content disposition
        StringBuilder disposition = new StringBuilder();
        disposition.append("inline; ");
        disposition.append("filename=\"");
        disposition.append(StringUtils.trimToEmpty(binaryContent.getName()));
        disposition.append("\"");
        content.setDisposition(disposition.toString());

        // File
        File file = binaryContent.getFile();
        content.setFile(file);

        return content;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void drop(PortalControllerContext portalControllerContext, List<String> sourceIdentifiers, String targetIdentifier) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Portlet response
        PortletResponse response = portalControllerContext.getResponse();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        try {
            // Move
            this.repository.move(portalControllerContext, sourceIdentifiers, targetIdentifier);

            // Notification
            String message = bundle.getString("FILE_BROWSER_MOVE_SUCCESS_MESSAGE");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        } catch (NuxeoException e) {
            // Notification
            String message = bundle.getString("FILE_BROWSER_MOVE_WARNING_MESSAGE");
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.WARNING);
        }

        // Refresh navigation
        request.setAttribute(Constants.PORTLET_ATTR_UPDATE_CONTENTS, Constants.PORTLET_VALUE_ACTIVATE);

        // Update public render parameter for associated portlets refresh
        if (response instanceof ActionResponse) {
            ActionResponse actionResponse = (ActionResponse) response;
            actionResponse.setRenderParameter("dnd-update", String.valueOf(System.currentTimeMillis()));
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void upload(PortalControllerContext portalControllerContext, FileBrowserForm form) throws PortletException, IOException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        // Upload multipart files
        List<MultipartFile> upload = form.getUpload();

        if (CollectionUtils.isNotEmpty(upload)) {
            try {
                // Import
                this.repository.importFiles(portalControllerContext, upload);

                // Notification
                String message = bundle.getString("FILE_BROWSER_UPLOAD_SUCCESS_MESSAGE");
                this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
            } catch (NuxeoException e) {
                // Notification
                String message = bundle.getString("FILE_BROWSER_UPLOAD_ERROR_MESSAGE");
                this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.ERROR);
            }

            // Refresh navigation
            request.setAttribute(Constants.PORTLET_ATTR_UPDATE_CONTENTS, Constants.PORTLET_VALUE_ACTIVATE);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMenubar(PortalControllerContext portalControllerContext) throws PortletException {
        this.repository.updateMenubar(portalControllerContext);
    }

}
