package fr.gouv.education.foad.integrity.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.gouv.education.foad.integrity.service.IntegrityService;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * View controller.
 *
 * @author Loïc Billon
 * @see CMSPortlet
 * @see PortletConfigAware
 * @see PortletContextAware
 */
@Controller
@RequestMapping(value = "VIEW")
public class ViewController extends CMSPortlet implements PortletConfigAware, PortletContextAware {

    /** Portlet config. */
    private PortletConfig portletConfig;
    /** Portlet context. */
    private PortletContext portletContext;

    /** service. */
    @Autowired
    private IntegrityService service;
    
    /** Bundle factory. */
    @Autowired
    protected IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    protected INotificationsService notificationsService;    


    /**
     * Constructor.
     */
    public ViewController() {
        super();
    }


    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }


    /**
     * Pre-destroy.
     */
    @PreDestroy
    public void preDestroy() {
        super.destroy();
    }


    /**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) {
        return "view";
    }


    /**
     * Exception handler.
     *
     * @param request portlet request
     * @param response portlet response
     * @return error path
     * @throws PortletException
     */
    @ExceptionHandler
    public String exceptionHandler(PortletRequest request, PortletResponse response) throws PortletException {
        return "error";
    }


    /**
     * 
     *
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "checkIntegrity")
    public void checkIntegrity(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        String repare = request.getParameter("repare");
        
        this.service.checkIntegrity(portalControllerContext, BooleanUtils.toBoolean(repare));
    }
    
    /**
     * 
     *
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "purgeInvit")    
    public void purgeInvit(ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

    	String testStr = request.getParameter("test");
    	
    	boolean test = BooleanUtils.toBoolean(testStr);
    	if(test) { 
    		this.service.purgeInvit(portalControllerContext, test);
    	}
    	else {
    		this.service.purgeAllInvit(portalControllerContext);
    	}
    	
    }
    
    /**
     * Purge users
     *
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "purgeUsers")
    public void purgeUsers(@ModelAttribute PurgeUsersForm form, ActionRequest request, ActionResponse response) throws PortletException {
        // Portal controller context
    	PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
		IBundleFactory bundleFactory2 = getBundleFactory();
		Bundle bundle = bundleFactory2.getBundle(null);

        String button = request.getParameter("btnName");
    	boolean test = true;
    	if(StringUtils.isNotBlank(button) && "run".equals(button)) {
    		test = false;
    	}
    	
    	if(StringUtils.isNotBlank(form.getLogins())) {
			
			String[] split = form.getLogins().split(";");
			List<String> logins = new ArrayList<String>();
					
			for(int i = 0; i < split.length; i++) {
				String trim = StringUtils.trim(split[i]);
				if(StringUtils.isNotBlank(trim)) {
					logins.add(trim);
				}
			}
			
	        this.service.purgeUsers(pcc, logins, test);

		}
    	else {
			getNotificationsService().addSimpleNotification(pcc, bundle.getString("PURGE_LOGINS_ERROR"), NotificationsType.ERROR);

    	}
    	
    	
    }
    


    @ActionMapping(value = "chgValidDate")
    public void chgValidDate(@ModelAttribute ChgValidDateForm form, ActionRequest request, ActionResponse response) {
    	
    	PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
		IBundleFactory bundleFactory2 = getBundleFactory();
		Bundle bundle = bundleFactory2.getBundle(null);
    	
    	String button = request.getParameter("btnName");
    	boolean test = true;
    	if(StringUtils.isNotBlank(button) && "run".equals(button)) {
    		test = false;
    	}
    	
    	Date validity = null;
    	Date current = null;
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		try {
			validity = sdf.parse(form.getValidityDate());
		} catch (ParseException e) {

			getNotificationsService().addSimpleNotification(pcc, bundle.getString("VALIDITY_DATE_ERROR"), NotificationsType.ERROR);
			
		}
		
		if(validity != null) {
		
	    	if(StringUtils.isNotBlank(form.getCurrentDate())) {
	    		try {
	    			current = sdf.parse(form.getCurrentDate());
				} catch (ParseException e) {

					getNotificationsService().addSimpleNotification(pcc, bundle.getString("CURRENT_DATE_ERROR"), NotificationsType.ERROR);
					
				}
	    		
	    	} 
	    	if((current != null && StringUtils.isNotBlank(form.getLogins())) || (current == null && StringUtils.isBlank(form.getLogins()))) {

				getNotificationsService().addSimpleNotification(pcc, bundle.getString("METHOD_NOT_CHOOSE"), NotificationsType.ERROR);
	    	}
	    	if(current != null) {
	    		service.chgValidDate(pcc, validity, current, test);
	    	}
	    	else {
	    		if(StringUtils.isNotBlank(form.getLogins())) {
	    			
	    			String[] split = form.getLogins().split(";");
	    			List<String> logins = new ArrayList<String>();
	    					
	    			for(int i = 0; i < split.length; i++) {
	    				String trim = StringUtils.trim(split[i]);
	    				if(StringUtils.isNotBlank(trim)) {
	    					logins.add(trim);
	    				}
	    			}
	    			
	    			service.chgValidDate(pcc, validity, logins, test);
	
	    		}
	    	}

		}
    }
    
    /**
     * 
     * @return
     */
    @ModelAttribute
    public ChgValidDateForm getChgValidDateForm() {
    	return new ChgValidDateForm();
    	
    }
    
    
    /**
     * 
     * @return
     */
    @ModelAttribute
    public PurgeUsersForm getPurgeUsersForm() {
    	return new PurgeUsersForm();
    	
    }
        

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        this.portletConfig = portletConfig;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletContext(PortletContext portletContext) {
        this.portletContext = portletContext;
    }

}
