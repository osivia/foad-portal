package fr.gouv.education.foad.customizer.project;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.portal.core.model.portal.Page;
import org.jboss.portal.core.model.portal.Window;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.customization.CustomizationModuleMetadatas;
import org.osivia.portal.api.customization.ICustomizationModule;
import org.osivia.portal.api.customization.ICustomizationModulesRepository;
import org.osivia.portal.api.customization.IProjectCustomizationConfiguration;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.core.constants.InternalConstants;

/**
 * Project customizer.
 *
 * @author Cédric Krommenhoek
 * @see GenericPortlet
 * @see ICustomizationModule
 */
public class ProjectCustomizer extends GenericPortlet implements ICustomizationModule {

    /** Customizer name. */
    private static final String CUSTOMIZER_NAME = "foad.customizer.project";
    /** Customization modules repository attribute name. */
    private static final String ATTRIBUTE_CUSTOMIZATION_MODULES_REPOSITORY = "CustomizationModulesRepository";

    /** First connection indicator window property name. */
    private static final String FIRST_CONNECTION_INDICATOR_PROPERTY = "first-connection";


    /** Portal URL factory. */
    private final IPortalUrlFactory portalUrlFactory;
    /** Person service. */
    private final PersonService personService;
    /** Internationalization bundle factory. */
    private final IBundleFactory bundleFactory;

    /** Customization module metadatas. */
    private final CustomizationModuleMetadatas metadatas;


    /** Customization modules repository. */
    private ICustomizationModulesRepository repository;


    /**
     * Constructor.
     */
    public ProjectCustomizer() {
        super();

        // Portal URL factory
        this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
        // Person service
        this.personService = DirServiceFactory.getService(PersonService.class);
        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());

        // Customization module metadata
        this.metadatas = new CustomizationModuleMetadatas();
        this.metadatas.setName(CUSTOMIZER_NAME);
        this.metadatas.setModule(this);
        this.metadatas.setCustomizationIDs(Arrays.asList(IProjectCustomizationConfiguration.CUSTOMIZER_ID));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void init() throws PortletException {
        this.repository = (ICustomizationModulesRepository) this.getPortletContext().getAttribute(ATTRIBUTE_CUSTOMIZATION_MODULES_REPOSITORY);
        this.repository.register(this.metadatas);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        this.repository.unregister(this.metadatas);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customize(String customizationId, CustomizationContext customizationContext) {
        // Portal controller context
        PortalControllerContext portalControllerContext = customizationContext.getPortalControllerContext();
        // Customization attributes
        Map<String, Object> attributes = customizationContext.getAttributes();
        // Project customization configuration
        IProjectCustomizationConfiguration configuration = (IProjectCustomizationConfiguration) attributes
                .get(IProjectCustomizationConfiguration.CUSTOMIZER_ATTRIBUTE_CONFIGURATION);
        // HTTP servlet request
        HttpServletRequest servletRequest = configuration.getHttpServletRequest();
        // Principal
        Principal principal = servletRequest.getUserPrincipal();
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(customizationContext.getLocale());

        if (configuration.isBeforeInvocation() && (principal != null)) {
            firstConnectionRedirection(portalControllerContext, configuration, principal, bundle);
        }
    }


    /**
     * First connection redirection.
     * 
     * @param portalControllerContext portal controller context
     * @param configuration project customization configuration
     * @param principal user principal
     * @param bundle internationalization bundle
     */
    private void firstConnectionRedirection(PortalControllerContext portalControllerContext, IProjectCustomizationConfiguration configuration,
            Principal principal, Bundle bundle) {
        // Person
        Person person = this.personService.getPerson(principal.getName());

        if (StringUtils.isBlank(person.getDisplayName())) {
            // Page
            Page page = configuration.getPage();
            // Window
            Window window = page.getChild("virtual", Window.class);

            // Prevent loop on first connection portlet
            if ((window == null) || !BooleanUtils.toBoolean(window.getDeclaredProperty(FIRST_CONNECTION_INDICATOR_PROPERTY))) {
                // Page display name
                String displayName = bundle.getString("FIRST_CONNECTION_TITLE");

                // Window properties
                Map<String, String> properties = new HashMap<>();
                properties.put(InternalConstants.PROP_WINDOW_TITLE, displayName);
                properties.put("osivia.ajaxLink", "1");
                properties.put("theme.dyna.partial_refresh_enabled", String.valueOf(true));
                properties.put(FIRST_CONNECTION_INDICATOR_PROPERTY, String.valueOf(true));
                properties.put("osivia.services.firstConnection.redirectionUrl", StringEscapeUtils.escapeHtml(configuration.buildRestorableURL()));

                // Redirection URL
                String redirectionUrl;
                try {
                    redirectionUrl = this.portalUrlFactory.getStartPortletInNewPage(portalControllerContext, "first-connection", displayName,
                            "osivia-services-first-connection-instance", properties, null);
                } catch (PortalException e) {
                    throw new RuntimeException(e);
                }

                configuration.setRedirectionURL(redirectionUrl);
            }
        }
    }

}
