package fr.gouv.education.foad.customizer.attributes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.XMLConstants;
import javax.xml.namespace.QName;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.portal.core.controller.ControllerCommand;
import org.jboss.portal.core.controller.ControllerContext;
import org.jboss.portal.core.controller.ControllerException;
import org.jboss.portal.core.model.portal.Page;
import org.jboss.portal.core.model.portal.Window;
import org.jboss.portal.core.model.portal.command.render.RenderPageCommand;
import org.jboss.portal.core.model.portal.navstate.PageNavigationalState;
import org.jboss.portal.core.navstate.NavigationalStateContext;
import org.jboss.portal.core.theme.PageRendition;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.theming.IAttributesBundle;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSItem;
import org.osivia.portal.core.cms.CMSObjectPath;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.osivia.portal.core.context.ControllerContextAdapter;
import org.osivia.portal.core.portalobjects.PortalObjectUtils;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoConnectionProperties;

/**
 * Customized attributes bundle.
 *
 * @author Cédric Krommenhoek
 * @see IAttributesBundle
 */
public class CustomizedAttributesBundle implements IAttributesBundle {
	
	/** . */
    private static final String FIM_URL_RETOUR = "freduurlretour";

    /** SSO applications attribute name. */
    private static final String APPLICATIONS = "osivia.sso.applications";
    /** CGU URL. */
    private static final String CGU_URL = "cgu.url";
    /** Help FAQ URL. */
    private static final String HELP_FAQ_URL = "help.faq.url";
    /** Help tutorials URL. */
    private static final String HELP_TUTORIALS_URL = "help.tutorials.url";
    /** Help contact URL. */
    private static final String HELP_CONTACT_URL = "help.contact.url";

    /** Piwik url */
	private static final String STATS_SERVER_URL = "stats.server.url";
	/** Piwik siteId -identifiant de Tribu dans piwik */
	private static final String STATS_SERVER_SITEID = "stats.server.siteid";
	/** identifiant de la custom dimension Id de l'espace dans piwik */
	private static final String STATS_DIM_SPACEID = "stats.dim.spaceid";
	/** identifiant de la custom dimension Titre de l'espace dans piwik */
	private static final String STATS_DIM_SPACETITLE = "stats.dim.spacetitle";
	
    /** Current space title. */
    private static final String SPACE_TITLE = "stats.space.title";
    /** Current space identifier. */
    private static final String SPACE_ID = "stats.space.id";
    
    /** Current workspace tabs. */
    private static final String WORKSPACE_TABS = "workspace.tabs";
    /** Page content title. */
    private static final String PAGE_CONTENT_TITLE = "page.content.title";

    
    /** Singleton instance. */
    private static final IAttributesBundle INSTANCE = new CustomizedAttributesBundle();


    /** Log. */
    private final Log log;

    /** Attribute names. */
    private final Set<String> names;

    /** SSO applications. */
    private final List<String> applications;

    /** Portal URL factory. */
    private final IPortalUrlFactory portalUrlFactory;
    /** CMS service locator. */
    private final ICMSServiceLocator cmsServiceLocator;
    /** Internationalization bundle factory. */
    private final IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    private CustomizedAttributesBundle() {
        super();

        // Log
        this.log = LogFactory.getLog(this.getClass());

        // Attributes names
        this.names = new HashSet<String>();
        this.names.add(APPLICATIONS);
        this.names.add(CGU_URL);
        this.names.add(HELP_FAQ_URL);
        this.names.add(HELP_TUTORIALS_URL);
        this.names.add(HELP_CONTACT_URL);
        this.names.add(STATS_SERVER_URL);
        this.names.add(STATS_SERVER_SITEID);
        this.names.add(STATS_DIM_SPACEID);
        this.names.add(STATS_DIM_SPACETITLE);
        this.names.add(SPACE_TITLE);
        this.names.add(SPACE_ID);
        this.names.add(WORKSPACE_TABS);
        this.names.add(PAGE_CONTENT_TITLE);

        // SSO applications
        this.applications = new ArrayList<String>();
        this.applications.add(NuxeoConnectionProperties.getPublicBaseUri().toString().concat("/logout"));
        this.applications.add(System.getProperty("cas.logout"));

        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
        // CMS service locator
        this.cmsServiceLocator = Locator.findMBean(ICMSServiceLocator.class, ICMSServiceLocator.MBEAN_NAME);
        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
    }


    /**
     * Get singleton instance.
     *
     * @return instance
     */
    public static IAttributesBundle getInstance() {
        return INSTANCE;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void fill(RenderPageCommand renderPageCommand, PageRendition pageRendition, Map<String, Object> attributes) throws ControllerException {
        // Controller context
        ControllerContext controllerContext = renderPageCommand.getControllerContext();
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(controllerContext);

        // Page
        Page page = renderPageCommand.getPage();

        // Current document
        Document document = this.getCurrentDocument(renderPageCommand);
        // Current root document
        Document rootDocument = this.getCurrentRootDocument(controllerContext, document);


        // SSO applications
        List<String> applisToLogout = new ArrayList<String>(applications);
        if (portalControllerContext != null && portalControllerContext.getHttpServletRequest() != null) {
            String headerUrlRetour = portalControllerContext.getHttpServletRequest().getHeader(FIM_URL_RETOUR);
            if (StringUtils.isNotBlank(headerUrlRetour)) {
                // Si Header FIM présent, logout portal en ajax + redirection vers FIM
                // Sinon, logout classique portail
                String portalLogout = (String) attributes.get(Constants.ATTR_TOOLBAR_SIGN_OUT_URL);
                applisToLogout.add(portalLogout);
                attributes.put(Constants.ATTR_TOOLBAR_SIGN_OUT_URL, headerUrlRetour);

            }
        }
        attributes.put(APPLICATIONS, applisToLogout);


        // CGU
        String cguUrl = this.getUrlFromProperty(portalControllerContext, "osivia.services.cgu.path");
        attributes.put(CGU_URL, cguUrl);


        // Help FAQ
        String faqUrl = this.getUrlFromProperty(portalControllerContext, "help.faq.path");
        attributes.put(HELP_FAQ_URL, faqUrl);

        // Help tutorials
        String tutorialsUrl = this.getUrlFromProperty(portalControllerContext, "help.tutorials.path");
        attributes.put(HELP_TUTORIALS_URL, tutorialsUrl);

        // Help contact
        String contactUrl;
        String mailto = System.getProperty("help.contact.mailto");
        if (StringUtils.isBlank(mailto)) {
            contactUrl = null;
        } else {
            StringBuilder builder = new StringBuilder();
            builder.append("mailto:");
            builder.append(mailto);

            // Subject
            String subject = System.getProperty("help.contact.subject");
            if (StringUtils.isNotBlank(subject)) {
                builder.append("?subject=");
                builder.append(StringEscapeUtils.escapeHtml(subject));
            }

            contactUrl = builder.toString();
        }
        attributes.put(HELP_CONTACT_URL, contactUrl);


        if (rootDocument != null) {
            // Statistics
            this.computeStatistics(portalControllerContext, rootDocument, attributes);

            // Workspace tabs
            this.computeWorkspaceTabs(renderPageCommand, rootDocument, attributes);
        }

        // Page content title
        this.computePageContentTitle(portalControllerContext, page, document, attributes);
    }


    /**
     * Get current document.
     * 
     * @param renderPageCommand render page command
     * @return document, may be null
     */
    private Document getCurrentDocument(RenderPageCommand renderPageCommand) {
        // Controller context
        ControllerContext controllerContext = renderPageCommand.getControllerContext();

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setControllerContext(controllerContext);

        // Current page
        Page page = renderPageCommand.getPage();
        // Content path
        String path = this.getContentPath(controllerContext, page);

        // CMS item
        CMSItem cmsItem;
        if (StringUtils.isEmpty(path)) {
            cmsItem = null;
        } else {
            try {
                cmsItem = cmsService.getContent(cmsContext, path);
            } catch (CMSException e) {
                cmsItem = null;
            }
        }

        // Document
        Document document;
        if ((cmsItem != null) && (cmsItem.getNativeItem() instanceof Document)) {
            document = (Document) cmsItem.getNativeItem();
        } else {
            document = null;
        }

        return document;
    }


    /**
     * Get content path.
     * 
     * @param controllerContext controller context
     * @param page page
     * @return path
     */
    private String getContentPath(ControllerContext controllerContext, Page page) {
        NavigationalStateContext nsContext = (NavigationalStateContext) controllerContext.getAttributeResolver(ControllerCommand.NAVIGATIONAL_STATE_SCOPE);
        PageNavigationalState pageState = nsContext.getPageNavigationalState(page.getId().toString());

        String[] sPath = null;
        if (pageState != null) {
            sPath = pageState.getParameter(new QName(XMLConstants.DEFAULT_NS_PREFIX, "osivia.cms.contentPath"));
        }

        String pathPublication = null;
        if ((sPath != null) && (sPath.length > 0)) {
            pathPublication = sPath[0];
        }

        return pathPublication;
    }


    /**
     * Get current root document.
     * 
     * @param controllerContext controller context
     * @param document current document, may be null
     * @return root document, may be null
     */
    private Document getCurrentRootDocument(ControllerContext controllerContext, Document document) {
        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setControllerContext(controllerContext);

        // CMS item
        CMSItem cmsItem;
        if (document == null) {
            cmsItem = null;
        } else {
            // Path
            String path;
            // Document type
            DocumentType type;

            try {
                path = document.getPath();
                cmsItem = cmsService.getSpaceConfig(cmsContext, path);
                type = cmsItem.getType();

                while ((type != null) && !type.isRootType()) {
                    // Get the navigation parent
                    CMSObjectPath parent = CMSObjectPath.parse(path).getParent();
                    path = parent.toString();

                    cmsItem = cmsService.getSpaceConfig(cmsContext, path);
                    type = cmsItem.getType();
                }
            } catch (CMSException e) {
                cmsItem = null;
            }
        }

        // Root document
        Document rootDocument;
        if ((cmsItem != null) && (cmsItem.getNativeItem() instanceof Document)) {
            rootDocument = (Document) cmsItem.getNativeItem();
        } else {
            rootDocument = null;
        }

        return rootDocument;
    }


    /**
     * Compute statistics.
     * 
     * @param portalControllerContext portal controller context
     * @param rootDocument root document
     * @param attributes attributes map
     * @throws ControllerException
     */
    private void computeStatistics(PortalControllerContext portalControllerContext, Document rootDocument, Map<String, Object> attributes)
            throws ControllerException {
        String spaceTitle = rootDocument.getTitle();
        attributes.put(SPACE_TITLE, spaceTitle);

        String spaceId = rootDocument.getString("webc:url");
        attributes.put(SPACE_ID, spaceId);

        if (StringUtils.isNotBlank(System.getProperty(STATS_SERVER_URL))) {
            attributes.put(STATS_SERVER_URL, System.getProperty(STATS_SERVER_URL));
        }

        if (StringUtils.isNotBlank(System.getProperty(STATS_SERVER_SITEID))) {
            attributes.put(STATS_SERVER_SITEID, System.getProperty(STATS_SERVER_SITEID));
        }

        if (StringUtils.isNotBlank(System.getProperty(STATS_DIM_SPACEID))) {
            attributes.put(STATS_DIM_SPACEID, System.getProperty(STATS_DIM_SPACEID));
        }

        if (StringUtils.isNotBlank(System.getProperty(STATS_DIM_SPACETITLE))) {
            attributes.put(STATS_DIM_SPACETITLE, System.getProperty(STATS_DIM_SPACETITLE));
        }
    }


    /**
     * Compute workspace tabs.
     * 
     * @param renderPageCommand render page command
     * @param rootDocument root document
     * @param attributes attributes map
     */
    private void computeWorkspaceTabs(RenderPageCommand renderPageCommand, Document rootDocument, Map<String, Object> attributes) {
        // Controller context
        ControllerContext controllerContext = renderPageCommand.getControllerContext();
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(controllerContext);

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setControllerContext(controllerContext);

        // Current page
        Page page = renderPageCommand.getPage();
        // Current CMS base path
        String basePath = page.getProperty("osivia.cms.basePath");

        if ("Workspace".equals(rootDocument.getType())) {
            // Rooms
            List<Document> rooms = this.getRooms(cmsService, cmsContext, rootDocument.getPath(), rootDocument.getPath());

            // Tabs
            List<WorkpaceTab> tabs;
            if (CollectionUtils.isEmpty(rooms)) {
                tabs = new ArrayList<>(1);
            } else {
                tabs = new ArrayList<>(rooms.size() + 1);
            }

            // Workspace tab
            WorkpaceTab workspaceTab = this.createWorkspaceTab(portalControllerContext, rootDocument, basePath);
            tabs.add(workspaceTab);

            // Room tabs
            if (CollectionUtils.isNotEmpty(rooms)) {
                for (Document room : rooms) {
                    WorkpaceTab roomTab = this.createWorkspaceTab(portalControllerContext, room, basePath);
                    tabs.add(roomTab);
                }
            }

            attributes.put(WORKSPACE_TABS, tabs);
        }
    }


    /**
     * Get rooms.
     * 
     * @param cmsService CMS service
     * @param cmsContext CMS context
     * @param rootPath root path
     * @param parentPath parent path
     * @return rooms, may be null
     */
    private List<Document> getRooms(ICMSService cmsService, CMSServiceCtx cmsContext, String rootPath, String parentPath) {
        List<CMSItem> navigationItems;
        try {
            navigationItems = cmsService.getPortalNavigationSubitems(cmsContext, rootPath, parentPath);
        } catch (CMSException e) {
            this.log.error(e.getMessage(), e.getCause());
            navigationItems = null;
        }

        List<Document> rooms;
        if (CollectionUtils.isEmpty(navigationItems)) {
            rooms = null;
        } else {
            rooms = new ArrayList<>();

            for (CMSItem navigationItem : navigationItems) {
                if (navigationItem.getNativeItem() instanceof Document) {
                    Document document = (Document) navigationItem.getNativeItem();

                    if ("Room".equals(document.getType()) && BooleanUtils.isTrue(document.getProperties().getBoolean("ttc:showInMenu"))) {
                        rooms.add(document);

                        List<Document> children = this.getRooms(cmsService, cmsContext, rootPath, document.getPath());
                        if (CollectionUtils.isNotEmpty(children)) {
                            rooms.addAll(children);
                        }
                    }
                }
            }
        }

        return rooms;
    }


    /**
     * Create workspace tab.
     * 
     * @param document document
     * @param currentPath current path
     * @return workspace tab
     */
    private WorkpaceTab createWorkspaceTab(PortalControllerContext portalControllerContext, Document document, String currentPath) {
        WorkpaceTab tab = new WorkpaceTab();

        // Title
        tab.setTitle(document.getTitle());

        // URL
        String url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, document.getPath(), null, null, null, null, null, null, null);
        tab.setUrl(url);

        // Active tab indicator
        boolean active = StringUtils.equals(currentPath, document.getPath());
        tab.setActive(active);

        // Home indicator
        boolean home = "Workspace".equals(document.getType());
        tab.setHome(home);

        return tab;
    }


    /**
     * Compute page content title.
     * 
     * @param portalControllerContext portal controller context
     * @param page page
     * @param document current document, may be null
     * @param attributes attributes map
     */
    private void computePageContentTitle(PortalControllerContext portalControllerContext, Page page, Document document, Map<String, Object> attributes) {
        // Controller context
        ControllerContext controllerContext = ControllerContextAdapter.getControllerContext(portalControllerContext);
        // HTTP servlet request
        HttpServletRequest httpServletRequest = portalControllerContext.getHttpServletRequest();
        // Locale
        Locale locale = httpServletRequest.getLocale();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(locale);
        
        // Maximized window
        Window maximizedWindow = PortalObjectUtils.getMaximizedWindow(controllerContext, page);
        
        
        // Content title
        String contentTitle;

        if (maximizedWindow != null) {
            contentTitle = null;
        } else if ((document != null) && ("Workspace".equals(document.getType()) || "Room".equals(document.getType()))) {
            contentTitle = StringUtils.trimToNull(document.getString("ttcs:welcomeTitle"));

            if ("Workspace".equals(document.getType())) {
                contentTitle = StringUtils.defaultIfEmpty(contentTitle, bundle.getString("WORKSPACE_WELCOME_TITLE_DEFAULT"));
            }
        } else if (StringUtils.startsWith(page.getName(), "home-")) {
            contentTitle = bundle.getString("HOME_CONTENT_TITLE");
        } else {
            contentTitle = null;
        }

        attributes.put(PAGE_CONTENT_TITLE, contentTitle);
    }


    /**
     * Get URL from system property.
     * 
     * @param portalControllerContext portal controller context
     * @param property system property
     * @return URL
     */
    private String getUrlFromProperty(PortalControllerContext portalControllerContext, String property) {
        // Path
        String path = System.getProperty(property);

        // URL
        String url;

        if (StringUtils.isBlank(path)) {
            url = null;
        } else {
            if (!StringUtils.startsWith(path, "/")) {
                // WebId
                path = NuxeoController.webIdToCmsPath(path);
            }

            url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, null, null, null, null, null, null, null);
        }

        return url;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getAttributeNames() {
        return this.names;
    }

}
