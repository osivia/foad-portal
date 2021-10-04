package fr.gouv.education.foad.generator.controller;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.gouv.education.foad.generator.model.Configuration;
import fr.gouv.education.foad.generator.model.GenerateForm;
import fr.gouv.education.foad.generator.service.GeneratorService;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * View generator controller.
 *
 * @author Cédric Krommenhoek
 * @see CMSPortlet
 * @see PortletConfigAware
 * @see PortletContextAware
 */
@Controller
@RequestMapping(value = "VIEW")
public class ViewGeneratorController extends CMSPortlet implements PortletConfigAware, PortletContextAware {

    /** Portlet config. */
    private PortletConfig portletConfig;
    /** Portlet context. */
    private PortletContext portletContext;

    /** Generator service. */
    @Autowired
    private GeneratorService service;


    /**
     * Constructor.
     */
    public ViewGeneratorController() {
        super();
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
     * Generate data action mapping.
     *
     * @param request action request
     * @param response action response
     * @throws PortletException
     */
    @ActionMapping(value = "generate")
    public void generate(ActionRequest request, ActionResponse response, @ModelAttribute("generateForm") GenerateForm form) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        String button = request.getParameter("btnName");
    	boolean generate = false;
    	if(StringUtils.isNotBlank(button) && ("generate".equals(button))) {
    		generate = true;
    	}
    	if(generate) {
    		this.service.generate(portalControllerContext, form);	
    	}
    	else {
    		this.service.purge(portalControllerContext, form);
    	}
        
        
    }

    /**
     * Get generator configuration model attribute.
     *
     * @param request portlet request
     * @param response portlet response
     * @return configuration
     * @throws PortletException
     */
    @ModelAttribute(value = "configuration")
    public Configuration getConfiguration(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);

        return this.service.getConfiguration(portalControllerContext);
    }
    
    @ModelAttribute("generateForm")
    public GenerateForm getForm() {
    	return new GenerateForm();
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
