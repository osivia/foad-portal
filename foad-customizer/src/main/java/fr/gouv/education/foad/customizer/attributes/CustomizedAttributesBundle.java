package fr.gouv.education.foad.customizer.attributes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jboss.portal.core.controller.ControllerContext;
import org.jboss.portal.core.controller.ControllerException;
import org.jboss.portal.core.model.portal.Page;
import org.jboss.portal.core.model.portal.command.render.RenderPageCommand;
import org.jboss.portal.core.theme.PageRendition;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.theming.IAttributesBundle;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSItem;
import org.osivia.portal.core.cms.CMSObjectPath;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;

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
    /** Toolbar help URL. */
    private static final String TOOLBAR_HELP_URL = "toolbar.help.url";
    /** Current space title. */
    private static final String SPACE_TITLE = "stats.space.title";
    /** Current space identifier. */
    private static final String SPACE_ID = "stats.space.id";

    /** Singleton instance. */
    private static final IAttributesBundle INSTANCE = new CustomizedAttributesBundle();


    /** Attribute names. */
    private final Set<String> names;

    /** SSO applications. */
    private final List<String> applications;

    /** Portal URL factory. */
    private final IPortalUrlFactory portalUrlFactory;
    /** CMS service locator. */
    private final ICMSServiceLocator cmsServiceLocator;


    /**
     * Constructor.
     */
    private CustomizedAttributesBundle() {
        super();

        // Attributes names
        this.names = new HashSet<String>();
        this.names.add(APPLICATIONS);
        this.names.add(TOOLBAR_HELP_URL);
        this.names.add(SPACE_TITLE);
        this.names.add(SPACE_ID);

        // SSO applications
        this.applications = new ArrayList<String>();
        this.applications.add(NuxeoConnectionProperties.getPublicBaseUri().toString().concat("/logout"));
        this.applications.add(System.getProperty("cas.logout"));

        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
        // CMS service locator
        this.cmsServiceLocator = Locator.findMBean(ICMSServiceLocator.class, ICMSServiceLocator.MBEAN_NAME);
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

        List<String> applisToLogout = new ArrayList<String>(applications);
        // SSO applications
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

        // Toolbar help URL
        String helpUrl;
        String helpPath = System.getProperty("help.path");
        if (StringUtils.isBlank(helpPath)) {
            helpUrl = null;
        } else {
            if (!StringUtils.startsWith(helpPath, "/")) {
                // WebId
                helpPath = NuxeoController.webIdToCmsPath(helpPath);
            }

            helpUrl = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, helpPath, null, null, null, null, null, null, null);
        }
        attributes.put(TOOLBAR_HELP_URL, helpUrl);

        // Statistics
        this.computeStatistics(renderPageCommand, attributes);
    }


    /**
     * Compute statistics.
     * 
     * @param renderPageCommand render page command
     * @param attributes attributes map
     * @throws ControllerException
     */
    private void computeStatistics(RenderPageCommand renderPageCommand, Map<String, Object> attributes) throws ControllerException {
        // Controller context
        ControllerContext controllerContext = renderPageCommand.getControllerContext();

        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setControllerContext(controllerContext);

        // Current page
        Page page = renderPageCommand.getPage();
        // Current CMS base path
        String basePath = page.getProperty("osivia.cms.basePath");

        // Space CMS item
        CMSItem cmsItem = null;
        if (StringUtils.isNotEmpty(basePath)) {
            // Path
            String path = basePath;
            // Document type
            DocumentType type;

            try {
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
                // Do nothing
            }
        }

        // Space document
        Document document;
        if ((cmsItem != null) && (cmsItem.getNativeItem() instanceof Document)) {
            document = (Document) cmsItem.getNativeItem();
        } else {
            document = null;
        }

        // Statistics data
        String spaceTitle;
        String spaceId;
        if (document == null) {
            spaceTitle = null;
            spaceId = null;
        } else {
            spaceTitle = document.getTitle();
            spaceId = document.getString("webc:url");
        }

        attributes.put(SPACE_TITLE, spaceTitle);
        attributes.put(SPACE_ID, spaceId);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getAttributeNames() {
        return this.names;
    }

}
