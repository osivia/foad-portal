package fr.gouv.education.foad.customizer.project;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.jboss.portal.core.controller.ControllerContext;
import org.jboss.portal.core.model.portal.Page;
import org.jboss.portal.core.model.portal.Window;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
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
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.core.constants.InternalConstants;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * Project customizer.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see ICustomizationModule
 */
public class ProjectCustomizer extends CMSPortlet implements ICustomizationModule {

    /** Customizer name. */
    private static final String CUSTOMIZER_NAME = "foad.customizer.project";
    /** Customization modules repository attribute name. */
    private static final String ATTRIBUTE_CUSTOMIZATION_MODULES_REPOSITORY = "CustomizationModulesRepository";

    /** First connection indicator window property name. */
    private static final String FIRST_CONNECTION_INDICATOR_PROPERTY = "first-connection";

    /** CGU level session attribute. */
    private static final String CGU_LEVEL_SESSION_ATTRIBUTE = "osivia.services.cgu.level";
    
    
	private static final String COOKIE_NEW_VERSION = "version4416Accepted";
	private static final String COOKIE_ROUTE = "ROUTEID";


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

	private INotificationsService notificationService;
	
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
        
		notificationService = Locator.findMBean(INotificationsService.class, INotificationsService.MBEAN_NAME);

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
        
        
//        	----- debug des headers ------- 
//        List<String> headerLogs = new ArrayList<String>();
//
//        Enumeration headerNames = servletRequest.getHeaderNames();
//        boolean display = false;
//        while(headerNames.hasMoreElements()) {
//        	String nextElement = (String) headerNames.nextElement();
//        	String header = servletRequest.getHeader(nextElement);
//        	headerLogs.add(nextElement + " >> " + header);
//        	
//        	if(nextElement.equalsIgnoreCase("x-forwarded-for") && !(header.equals("172.29.58.252") || header.equals("172.29.58.251"))) {
//        		display = true;
//        	}
//        }
//        if(display) {
//        	System.out.println("=========== Headers ===========");
//        	for(String log : headerLogs) {
//        		System.out.println(log);
//        	}
//        	System.out.println("======================");
//        }
//        
    	
    	
        
        // Principal
        Principal principal = servletRequest.getUserPrincipal();
        // Bundle
        Bundle bundle = this.bundleFactory.getBundle(customizationContext.getLocale());

        if (configuration.isBeforeInvocation() && (principal != null)) {
            this.firstConnectionRedirection(portalControllerContext, configuration, principal, bundle);

            if (StringUtils.isNotEmpty(configuration.getCMSPath())) {
                this.cguRedirection(portalControllerContext, configuration, principal, bundle);
            }
            
        }
//        if (configuration.isBeforeInvocation()) {
//        	oldVersionRedirection(customizationContext);
//        }
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

        if (person.getLastConnection() == null) {
            // Page
            Page page = configuration.getPage();
            // Window
            Window window;
            if (page == null) {
                window = null;
            } else {
                window = page.getChild("virtual", Window.class);
            }

            // Prevent loop on first connection portlet
            if ((window == null) || !BooleanUtils.toBoolean(window.getDeclaredProperty(FIRST_CONNECTION_INDICATOR_PROPERTY))) {
                // Page display name
                String displayName = bundle.getString("FIRST_CONNECTION_TITLE");

                // Window properties
                Map<String, String> properties = new HashMap<>();
                properties.put(InternalConstants.PROP_WINDOW_TITLE, displayName);
                properties.put("osivia.ajaxLink", "1");
                properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
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


    /**
     * CGU redirection.
     *
     * @param portalControllerContext portal controller context
     * @param configuration project customization configuration
     * @param principal user principal
     * @param bundle internationalization bundle
     */
    private void cguRedirection(PortalControllerContext portalControllerContext, IProjectCustomizationConfiguration configuration, Principal principal,
            Bundle bundle) {
        // Page
        Page page = configuration.getPage();

        if (page != null) {
            // Window
            Window window = page.getChild("virtual", Window.class);

            // HTTP servlet request
            HttpServletRequest servletRequest = configuration.getHttpServletRequest();
            // HTTP session
            HttpSession session = servletRequest.getSession();

            // Nuxeo controller
            NuxeoController nuxeoController = new NuxeoController(this.getPortletContext());
            nuxeoController.setServletRequest(servletRequest);


            // CGU path
            String path = page.getProperty("osivia.services.cgu.path");
            // Portal level
            String portalLevel = page.getProperty(CGU_LEVEL_SESSION_ATTRIBUTE);

            // Is CGU defined ?
            if ((portalLevel == null) || (path == null)) {
                return;
            }

            // CGU already checked (in session) ?
            String checkedLevel = String.valueOf(session.getAttribute(CGU_LEVEL_SESSION_ATTRIBUTE));
            if (StringUtils.equals(portalLevel, checkedLevel)) {
                return;
            }

            // No CGU request on CGU !!!
            if ((window != null) && StringUtils.isNotEmpty(window.getDeclaredProperty("osivia.services.cgu.path"))) {
                return;
            }


            // Get userProfile
            Document userProfile = (Document) nuxeoController.executeNuxeoCommand(new GetProfileCommand(principal.getName()));
            // User level
            String userLevel = userProfile.getProperties().getString("ttc_userprofile:terms_of_use_agreement");
            session.setAttribute(CGU_LEVEL_SESSION_ATTRIBUTE, userLevel);

            if (!portalLevel.equals(userLevel)) {
                session.setAttribute("osivia.services.cgu.pathToRedirect", configuration.buildRestorableURL());

                // Window properties
                Map<String, String> properties = new HashMap<String, String>();
                properties.put("osivia.services.cgu.path", path);
                properties.put(CGU_LEVEL_SESSION_ATTRIBUTE, portalLevel);
                properties.put("osivia.title", bundle.getString("CGU_TITLE"));
                properties.put("osivia.hideTitle", "1");
                // Redirection URL
                String redirectionUrl;
                try {
                    redirectionUrl = this.portalUrlFactory.getStartPortletInNewPage(portalControllerContext, "cgu", bundle.getString("CGU_TITLE_MINI"),
                            "osivia-services-cgu-portailPortletInstance", properties, null);
                } catch (PortalException e) {
                    throw new RuntimeException(e);
                }

                configuration.setRedirectionURL(redirectionUrl);
            }
        }
    }
    

//    /**
//	 * Check if redirection to the new version is needed.
//	 */
//	private void oldVersionRedirection(CustomizationContext ctx) {
//			
//		Map<String, Object> attributes = ctx.getAttributes();
//	
//	    // Project customization configuration
//	    IProjectCustomizationConfiguration configuration = (IProjectCustomizationConfiguration) attributes
//	            .get(IProjectCustomizationConfiguration.CUSTOMIZER_ATTRIBUTE_CONFIGURATION);
//	    
//	    HttpServletRequest request = configuration.getHttpServletRequest();
//		if(request != null) {
//			
//			// Lecture cookie
//			Cookie newVersionAccepted = null;
//			Cookie route = null;
//			
//			if(request.getCookies() != null) {
//				for(Cookie c : request.getCookies()) {
//					if(c.getName().equals(COOKIE_NEW_VERSION)) {
//						newVersionAccepted = c;
//					}
//					if(c.getName().equals(COOKIE_ROUTE)) {
//						route = c;
//					}
//				}
//			}
//			
//			HttpServletResponse response = configuration.getHttpServletResponse();
//		
//			// Si paramètre de requête transmis...
//			if(request.getParameter("oldVersion") != null) {
//				String oldVersion = request.getParameter("oldVersion");
//	
//				// Si choix utilisateut tranmis, suppression du cookie
//				Boolean booleanObject = BooleanUtils.toBooleanObject(oldVersion);
//			
//				newVersionAccepted = new Cookie(COOKIE_NEW_VERSION, booleanObject.toString());
//				newVersionAccepted.setMaxAge(0); // mise à 0 pour destruction du cookie
//				newVersionAccepted.setPath("/");
//				response.addCookie(newVersionAccepted);
//
//				changeRoute(ctx, configuration, route, response, booleanObject);
//				
//			}
//		
//	
//		}
//	}
//
//	/**
//	 * Change route to the portal hosting the old version 
//	 * @param ctx
//	 * @param configuration
//	 * @param route
//	 * @param response
//	 * @param booleanObject
//	 */
//	private void changeRoute(CustomizationContext ctx, IProjectCustomizationConfiguration configuration, Cookie route,
//			HttpServletResponse response, Boolean booleanObject) {
//		// Si ancienne version, modification de la route portail vers aller vers les anciens noeuds
//		if(booleanObject) {
//			route = new Cookie(COOKIE_ROUTE, "");
//			route.setPath("/");
//			route.setMaxAge(0);
//			response.addCookie(route);
//			
//			PortalControllerContext portalControllerContext = ctx.getPortalControllerContext();
//		    ControllerContext cc = (ControllerContext) portalControllerContext.getControllerCtx();
//		    String redirection = cc.getServerInvocation().getServerContext().getPortalContextPath();
//		    
//		    if(configuration.getCMSPath() != null) {
//		    	redirection = redirection + "/cms/" + configuration.getCMSPath();
//		    }
//		    						
//			configuration.setRedirectionURL(redirection);
//
//		}
//	}
}
