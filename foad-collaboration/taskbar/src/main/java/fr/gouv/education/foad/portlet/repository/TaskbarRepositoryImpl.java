package fr.gouv.education.foad.portlet.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.portlet.PortletException;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.cms.impl.BasicPermissions;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.menubar.MenubarContainer;
import org.osivia.portal.api.menubar.MenubarDropdown;
import org.osivia.portal.api.menubar.MenubarGroup;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSItem;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.gouv.education.foad.portlet.model.FolderTask;
import fr.gouv.education.foad.portlet.model.ServiceTask;
import fr.gouv.education.foad.portlet.model.comparator.FolderTaskComparator;
import fr.gouv.education.foad.portlet.repository.command.MoveDocumentsCommand;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * Taskbar portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see TaskbarRepository
 */
@Repository
public class TaskbarRepositoryImpl implements TaskbarRepository {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /** Taskbar service. */
    @Autowired
    private ITaskbarService taskbarService;

    /** Menubar service. */
    @Autowired
    private IMenubarService menubarService;

    /** CMS service locator. */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;

    /** Folder task comparator. */
    @Autowired
    private FolderTaskComparator folderTaskComparator;


    /**
     * Constructor.
     */
    public TaskbarRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskbarTask> getNavigationTasks(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Base path
        String basePath = nuxeoController.getBasePath();

        // Navigation tasks
        List<TaskbarTask> navigationTasks;
        if (StringUtils.isEmpty(basePath)) {
            navigationTasks = new ArrayList<TaskbarTask>(0);
        } else {
            try {
                navigationTasks = this.taskbarService.getTasks(portalControllerContext, basePath, true);
            } catch (PortalException e) {
                throw new PortletException(e);
            }
        }

        return navigationTasks;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<ServiceTask> getAdministration(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Workspace document context
        NuxeoDocumentContext workspaceDocumentContext = nuxeoController.getDocumentContext(nuxeoController.getBasePath());

        // Check permissions
        BasicPermissions permissions = workspaceDocumentContext.getPermissions(BasicPermissions.class);

        List<ServiceTask> administration;
        if (permissions.isManageableByUser()) {
            // Navbar
            Map<MenubarGroup, Set<MenubarItem>> navbar = this.menubarService.getNavbarSortedItems(portalControllerContext);
            // Dropdown
            MenubarContainer dropdown = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.CONFIGURATION_DROPDOWN_MENU_ID);

            // Menubar items
            Set<MenubarItem> menubarItems;
            if (MapUtils.isEmpty(navbar) || (dropdown == null)) {
                menubarItems = null;
            } else {
                menubarItems = navbar.get(dropdown.getGroup());
            }

            if (CollectionUtils.isEmpty(menubarItems)) {
                administration = null;
            } else {
                administration = new ArrayList<>(menubarItems.size());

                for (MenubarItem menubarItem : menubarItems) {
                    // Service
                    ServiceTask service = this.applicationContext.getBean(ServiceTask.class);

                    // Display name
                    service.setDisplayName(menubarItem.getTitle());
                    // URL
                    service.setUrl(menubarItem.getUrl());
                    // Icon
                    service.setIcon(menubarItem.getGlyphicon());

                    administration.add(service);
                }
            }
        } else {
            administration = null;
        }

        return administration;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void generateFolderNavigationTree(PortalControllerContext portalControllerContext, FolderTask folder) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        
        this.generateFolderChildren(nuxeoController, folder);
    }


    /**
     * Generate folder children.
     * 
     * @param nuxeoController Nuxeo controller
     * @param parent parent folder
     * @throws PortletException
     */
    private void generateFolderChildren(NuxeoController nuxeoController, FolderTask parent) throws PortletException {
        // Children
        SortedSet<FolderTask> children = this.getFolderChildren(nuxeoController, parent.getPath());
        parent.setChildren(children);
        
        if (CollectionUtils.isNotEmpty(children)) {
            for (FolderTask child : children) {
                // Children
                if (child.isSelected() || !child.isLazy()) {
                    this.generateFolderChildren(nuxeoController, child);
                }
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public SortedSet<FolderTask> getFolderChildren(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        return this.getFolderChildren(nuxeoController, path);
    }


    /**
     * Get folder children.
     * 
     * @param nuxeoController Nuxeo controller
     * @param folderPath folder path
     * @return folder children
     * @throws PortletException
     */
    private SortedSet<FolderTask> getFolderChildren(NuxeoController nuxeoController, String folderPath) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = nuxeoController.getPortalCtx();

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);

        // Base path
        String basePath = nuxeoController.getBasePath();
        // Current navigation path
        String currentNavigationPath = nuxeoController.getNavigationPath();


        // Navigation items
        List<CMSItem> navigationItems;
        try {
            navigationItems = cmsService.getPortalNavigationSubitems(cmsContext, basePath, folderPath);
        } catch (CMSException e) {
            throw new PortletException(e);
        }


        // Children
        SortedSet<FolderTask> children;
        if (CollectionUtils.isEmpty(navigationItems)) {
            children = null;
        } else {
            children = new TreeSet<>(this.folderTaskComparator);

            for (CMSItem navigationItem : navigationItems) {
                FolderTask child = this.applicationContext.getBean(FolderTask.class);

                // Document type
                DocumentType documentType = navigationItem.getType();
                // Fetched children indicator
                Boolean unfetchedChildren = BooleanUtils.toBooleanObject(navigationItem.getProperties().get("unfetchedChildren"));
                // Nuxeo document
                Document document = (Document) navigationItem.getNativeItem();

                // Identifier
                String id = document.getId();
                child.setId(id);

                // Path
                String path = navigationItem.getPath();
                child.setPath(path);

                // Display name
                String displayName = document.getTitle();
                child.setDisplayName(displayName);

                // URL
                String url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, null, null, "menu", null, null, "1", null);
                child.setUrl(url);

                // Active indicator
                boolean active = StringUtils.equals(currentNavigationPath, path);
                child.setActive(active);

                // Selected indicator
                boolean selected = this.isSelected(path, currentNavigationPath);
                child.setSelected(selected);

                // Folder indicator
                boolean folder = (documentType != null) && documentType.isFolderish();
                child.setFolder(folder);

                // Lazy indicator
                boolean lazy = (documentType != null) && documentType.isBrowsable() && !selected && BooleanUtils.isNotFalse(unfetchedChildren);
                child.setLazy(lazy);

                // Accepted types
                String[] acceptedTypes = this.getAcceptedTypes(navigationItem);
                child.setAcceptedTypes(acceptedTypes);

                children.add(child);
            }
        }

        return children;
    }


    /**
     * Check if path is a parent of current navigation path.
     * 
     * @param path path
     * @param currentNavigationPath current navigation path
     * @return true if path is a parent of current navigation path
     */
    private boolean isSelected(String path, String currentNavigationPath) {
        boolean result = StringUtils.startsWith(currentNavigationPath, path);

        // "/parent/child-2/foo" starts with "/parent/child", but it isn't a child
        if (result) {
            String[] splittedPath = StringUtils.split(path, "/");
            String[] splittedCurrentNavigationPath = StringUtils.split(currentNavigationPath, "/");

            int index = splittedPath.length - 1;
            result = StringUtils.equals(splittedPath[index], splittedCurrentNavigationPath[index]);
        }

        return result;
    }


    /**
     * Get accepted types.
     *
     * @param item CMS item
     * @return accepted types
     */
    private String[] getAcceptedTypes(CMSItem item) {
        String[] acceptedTypes = null;
        if ((item != null) && (item.getType() != null)) {
            List<String> types = item.getType().getPortalFormSubTypes();
            if (types != null) {
                acceptedTypes = types.toArray(new String[types.size()]);
            }
        }
        return acceptedTypes;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void moveDocuments(PortalControllerContext portalControllerContext, List<String> sourceIds, String targetId) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(MoveDocumentsCommand.class, sourceIds, targetId);
        nuxeoController.executeNuxeoCommand(command);
    }

}
