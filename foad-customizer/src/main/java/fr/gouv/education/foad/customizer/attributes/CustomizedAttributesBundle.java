package fr.gouv.education.foad.customizer.attributes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.jboss.portal.core.controller.ControllerContext;
import org.jboss.portal.core.controller.ControllerException;
import org.jboss.portal.core.model.portal.command.render.RenderPageCommand;
import org.jboss.portal.core.theme.PageRendition;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.theming.IAttributesBundle;
import org.osivia.portal.api.urls.IPortalUrlFactory;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoConnectionProperties;

/**
 * Customized attributes bundle.
 *
 * @author Cédric Krommenhoek
 * @see IAttributesBundle
 */
public class CustomizedAttributesBundle implements IAttributesBundle {

    /** SSO applications attribute name. */
    private static final String APPLICATIONS = "osivia.sso.applications";
    /** Toolbar help URL. */
    private static final String TOOLBAR_HELP_URL = "toolbar.help.url";

    /** Singleton instance. */
    private static final IAttributesBundle INSTANCE = new CustomizedAttributesBundle();


    /** Attribute names. */
    private final Set<String> names;

    /** SSO applications. */
    private final List<String> applications;

    /** Portal URL factory. */
    private final IPortalUrlFactory portalUrlFactory;


    /**
     * Constructor.
     */
    private CustomizedAttributesBundle() {
        super();

        // Attributes names
        this.names = new HashSet<String>();
        this.names.add(APPLICATIONS);
        this.names.add(TOOLBAR_HELP_URL);

        // SSO applications
        this.applications = new ArrayList<String>();
        this.applications.add(NuxeoConnectionProperties.getPublicBaseUri().toString().concat("/logout"));
        this.applications.add(System.getProperty("cas.logout"));

        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
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
        
        
        // SSO applications
        attributes.put(APPLICATIONS, this.applications);

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
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Set<String> getAttributeNames() {
        return this.names;
    }

}
