package fr.gouv.education.foad.customizer.plugin.menubar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.portlet.PortletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.cms.impl.BasicPermissions;
import org.osivia.portal.api.cms.impl.BasicPublicationInfos;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.menubar.MenubarContainer;
import org.osivia.portal.api.menubar.MenubarDropdown;
import org.osivia.portal.api.menubar.MenubarGroup;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.urls.Link;
import org.osivia.portal.core.constants.InternalConstants;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.liveedit.OnlyofficeLiveEditHelper;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

/**
 * FOAD menubar module.
 * 
 * @author Cédric Krommenhoek
 * @see MenubarModule
 */
public class FoadMenubarModule implements MenubarModule {

    /** Merged dropdown menu identifier. */
    private static final String MERGED_DROPDOWN_MENU_ID = "MERGED";


    /** Portal URL factory. */
    private final IPortalUrlFactory portalUrlFactory;
    /** Menubar service. */
    private final IMenubarService menubarService;
    /** Internationalization bundle factory. */
    private final IBundleFactory bundleFactory;
    /** Document DAO. */
    private final DocumentDAO documentDao;


    /**
     * Constructor.
     */
    public FoadMenubarModule() {
        super();

        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
        // Menubar service
        this.menubarService = Locator.findMBean(IMenubarService.class, IMenubarService.MBEAN_NAME);
        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
        // Document DAO
        this.documentDao = DocumentDAO.getInstance();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
            DocumentContext<? extends EcmDocument> spaceDocumentContext) throws PortalException {
        this.hideConfigurationItems(portalControllerContext, menubar);
        this.mergeDropdownMenus(portalControllerContext, menubar);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
            DocumentContext<? extends EcmDocument> documentContext) throws PortalException {
        this.removeItems(menubar, documentContext);
        this.mergeDropdownMenus(portalControllerContext, menubar);
        this.addLiveEditionItems(portalControllerContext, menubar, documentContext);
    }


    /**
     * Hide configuration menubar items.
     * 
     * @param portalControllerContext portal controller context
     * @param menubar menubar
     */
    private void hideConfigurationItems(PortalControllerContext portalControllerContext, List<MenubarItem> menubar) {
        // Configuration dropdown
        MenubarContainer dropdown = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.CONFIGURATION_DROPDOWN_MENU_ID);

        if (dropdown != null) {
            for (MenubarItem item : menubar) {
                MenubarContainer parent = item.getParent();
                if (((parent != null) && parent.equals(dropdown)) || "PRINT".equals(item.getId())) {
                    item.setVisible(false);
                }
            }
        }
    }


    /**
     * Remove menubar items.
     * 
     * @param menubar menubar
     * @param documentContext document context
     */
    private void removeItems(List<MenubarItem> menubar, DocumentContext<? extends EcmDocument> documentContext) {
        // Identifiers
        List<String> identifiers = Arrays.asList("SYNCHRONIZE_ACTION", "REMOTE_PUBLISHING_URL", "ADD_TOUTATICEPAD");
        BasicPublicationInfos publicationInfos = documentContext.getPublicationInfos(BasicPublicationInfos.class);
        if (publicationInfos.isDraft()) {
            identifiers.add("VALIDATION_WF_URL");
        }

        // Removed items
        List<MenubarItem> removedItems = new ArrayList<>(identifiers.size());
        for (MenubarItem item : menubar) {
            if (identifiers.contains(item.getId())) {
                removedItems.add(item);
            }
        }

        menubar.removeAll(removedItems);
    }


    /**
     * Merge dropdown menus.
     * 
     * @param portalControllerContext portal controller context
     * @param menubar menubar
     */
    private void mergeDropdownMenus(PortalControllerContext portalControllerContext, List<MenubarItem> menubar) {
        // Dropdowns
        Map<MenubarDropdown, List<MenubarItem>> dropdowns = new HashMap<>();

        // Refresh item
        MenubarItem refresh = null;

        for (MenubarItem item : menubar) {
            MenubarContainer parent = item.getParent();

            if ("REFRESH".equals(item.getId())) {
                refresh = item;
            } else if (parent instanceof MenubarDropdown) {
                MenubarDropdown dropdown = (MenubarDropdown) parent;

                List<MenubarItem> dropdownItems = dropdowns.get(dropdown);
                if (dropdownItems == null) {
                    dropdownItems = new ArrayList<>();
                    dropdowns.put(dropdown, dropdownItems);
                }

                dropdownItems.add(item);
            }
        }

        // Increments
        Map<MenubarDropdown, Integer> increments = new HashMap<>();
        // CMS edition dropdown
        MenubarDropdown edition = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.CMS_EDITION_DROPDOWN_MENU_ID);
        if (edition != null) {
            increments.put(edition, 1000);
        }
        // Share dropdown
        MenubarDropdown share = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.SHARE_DROPDOWN_MENU_ID);
        if (share != null) {
            increments.put(share, 2000);
        }
        // Other options dropdown
        MenubarDropdown otherOptions = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.OTHER_OPTIONS_DROPDOWN_MENU_ID);
        if (otherOptions != null) {
            increments.put(otherOptions, 3000);
        }

        // Merged dropdown
        MenubarDropdown merged = this.menubarService.getDropdown(portalControllerContext, MERGED_DROPDOWN_MENU_ID);
        if (merged == null) {
            merged = new MenubarDropdown(MERGED_DROPDOWN_MENU_ID, null, "glyphicons glyphicons-option-vertical", MenubarGroup.GENERIC, 40);
            merged.setReducible(true);
            this.menubarService.addDropdown(portalControllerContext, merged);
        }

        for (Entry<MenubarDropdown, Integer> entry : increments.entrySet()) {
            MenubarDropdown dropdown = entry.getKey();
            List<MenubarItem> items = dropdowns.get(dropdown);
            Integer increment = increments.get(dropdown);
            if (CollectionUtils.isNotEmpty(items) && (increment != null)) {
                // Header
                MenubarItem header = new MenubarItem(dropdown.getId() + "_HEADER", dropdown.getTitle(), merged, increment - 100, null);
                header.setState(true);
                header.setDivider(true);
                menubar.add(header);

                for (MenubarItem item : items) {
                    item.setParent(merged);
                    item.setOrder(item.getOrder() + increment);
                    item.setDivider(false);
                }
            }
        }

        // Update refresh item
        if (refresh != null) {
            refresh.setParent(merged);
            refresh.setOrder(4000);
            refresh.setDivider(true);
        }
    }


    /**
     * Add live edition menubar items.
     * 
     * @param portalControllerContext portal controller context
     * @param menubar menubar
     * @param documentContext document context
     * @throws PortalException
     */
    private void addLiveEditionItems(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
            DocumentContext<? extends EcmDocument> documentContext) throws PortalException {
        if ((documentContext != null) && (documentContext instanceof NuxeoDocumentContext)) {
            NuxeoDocumentContext nuxeoDocumentContext = (NuxeoDocumentContext) documentContext;

            // Document type
            DocumentType documentType = nuxeoDocumentContext.getType();

            if ((documentType != null) && documentType.isFile()) {
                // Nuxeo controller
                NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
                // Portlet request
                PortletRequest request = portalControllerContext.getRequest();


                // Publication infos
                BasicPublicationInfos publicationInfos = nuxeoDocumentContext.getPublicationInfos(BasicPublicationInfos.class);
                // Permissions
                BasicPermissions permissions = nuxeoDocumentContext.getPermissions(BasicPermissions.class);
                // Document DTO
                DocumentDTO documentDto = this.documentDao.toDTO(nuxeoDocumentContext.getDoc());

                // Internationalization bundle
                Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

                // Live edition dropdown
                MenubarDropdown liveEditionDropdown = new MenubarDropdown("LIVE_EDITION", null, "glyphicons glyphicons-pencil", MenubarGroup.CMS, 6);
                liveEditionDropdown.setReducible(false);
                this.menubarService.addDropdown(portalControllerContext, liveEditionDropdown);


                // OnlyOffice
                if (documentDto.isLiveEditable()) {
                    if (permissions.isEditableByUser()) {
                        String onlyOfficeTitle = bundle.getString("MENUBAR_ONLYOFFICE_TITLE");

                        // OnlyOffice (with lock)
                        String onlyOfficeWithLockText = bundle.getString("MENUBAR_ONLYOFFICE_WITH_LOCK");
                        String onlyOfficeWithLockUrl = this.getOnlyOfficeUrl(portalControllerContext, documentDto.getPath(), onlyOfficeTitle, true);
                        MenubarItem onlyOfficeWithLock = new MenubarItem("ONLYOFFICE_WITH_LOCK", onlyOfficeWithLockText, null, liveEditionDropdown, 1,
                                onlyOfficeWithLockUrl, null, null, null);
                        menubar.add(onlyOfficeWithLock);

                        // OnlyOffice (without lock)
                        String onlyOfficeWithoutLockText = bundle.getString("MENUBAR_ONLYOFFICE_WITHOUT_LOCK");
                        String onlyOfficeWithoutLockUrl = this.getOnlyOfficeUrl(portalControllerContext, documentDto.getPath(), onlyOfficeTitle, false);
                        MenubarItem onlyOfficeWithoutLock = new MenubarItem("ONLYOFFICE_WITHOUT_LOCK", onlyOfficeWithoutLockText,
                                null, liveEditionDropdown, 2, onlyOfficeWithoutLockUrl, null, null, null);
                        menubar.add(onlyOfficeWithoutLock);
                    } else if (StringUtils.isNotEmpty(request.getRemoteUser())) {
                        // OnlyOffice (read only)
                        String onlyOfficeTitle = bundle.getString("MENUBAR_ONLYOFFICE_READ_ONLY_TITLE");
                        String onlyOfficeReadOnlyText = bundle.getString("MENUBAR_ONLYOFFICE_READ_ONLY");
                        String onlyOfficeReadOnlyUrl = this.getOnlyOfficeUrl(portalControllerContext, documentDto.getPath(), onlyOfficeTitle, false);
                        MenubarItem onlyOfficeReadOnly = new MenubarItem("ONLYOFFICE_READ_ONLY", onlyOfficeReadOnlyText, null, liveEditionDropdown, 2,
                                onlyOfficeReadOnlyUrl, null, null, null);
                        menubar.add(onlyOfficeReadOnly);
                    }
                }


                // Nuxeo drive
                if (documentType.isLiveEditable() && permissions.isEditableByUser()
                        && (StringUtils.isNotEmpty(publicationInfos.getDriveUrl()) || publicationInfos.isDriveEnabled())) {
                    if (StringUtils.isNotEmpty(publicationInfos.getDriveUrl())) {
                        MenubarItem drive = new MenubarItem("DRIVE", bundle.getString("MENUBAR_DRIVE_EDIT"), null, liveEditionDropdown, 4,
                                publicationInfos.getDriveUrl(), null, null, null);
                        drive.setDivider(true);
                        menubar.add(drive);
                    } else {
                        // Drive not started warning
                        MenubarItem driveWarning = new MenubarItem("DRIVE_WARNING", bundle.getString("MENUBAR_DRIVE_NOT_STARTED_WARNING"), liveEditionDropdown,
                                3, null);
                        driveWarning.setDisabled(true);
                        driveWarning.setDivider(true);
                        menubar.add(driveWarning);

                        MenubarItem drive = new MenubarItem("DRIVE", bundle.getString("MENUBAR_DRIVE_EDIT"), null, liveEditionDropdown, 4, "#", null, null,
                                null);
                        drive.setDisabled(true);
                        menubar.add(drive);
                    }
                }


                // Download
                if (documentType.isFile()) {
                    Link link = nuxeoController.getLink(documentDto.getDocument(), "download");
                    MenubarItem download = new MenubarItem("DOWNLOAD", bundle.getString("MENUBAR_DOWNLOAD"), "glyphicons glyphicons-download-alt",
                            liveEditionDropdown, 5, link.getUrl(), "_blank", null, null);
                    download.setDivider(true);
                    menubar.add(download);
                }
            }
        }
    }


    /**
     * Get OnlyOffice URL.
     *
     * @param portalControllerContext portal controller context
     * @param path document path
     * @param title title
     * @param lock lock indicator
     * @return URL
     * @throws PortalException
     */
    private String getOnlyOfficeUrl(PortalControllerContext portalControllerContext, String path, String title, boolean lock) throws PortalException {
        // Window properties
        Map<String, String> properties = new HashMap<>();
        properties.put(Constants.WINDOW_PROP_URI, path);
        properties.put("osivia.hideTitle", String.valueOf(1));
        properties.put("osivia.onlyoffice.withLock", String.valueOf(lock));
        properties.put(InternalConstants.PROP_WINDOW_TITLE, title);

        return this.portalUrlFactory.getStartPortletUrl(portalControllerContext, OnlyofficeLiveEditHelper.ONLYOFFICE_PORTLET_INSTANCE, properties);
    }

}
