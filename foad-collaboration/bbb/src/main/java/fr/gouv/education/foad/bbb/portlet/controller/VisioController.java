package fr.gouv.education.foad.bbb.portlet.controller;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fr.gouv.education.foad.bbb.portlet.form.VisioForm;
import fr.gouv.education.foad.bbb.portlet.service.VisioException;
import fr.gouv.education.foad.bbb.portlet.service.VisioService;

/**
 * 
 * @author Loïc Billon
 *
 */
@Controller
@RequestMapping("VIEW")
@SessionAttributes("form")
public class VisioController {

	
	@Autowired
	private ApplicationContext context;
	
	@Autowired
	private VisioService service;
	
    @Autowired
    private PortletContext portletContext;	
	
    /**
     * View render mapping.
     * 
     */
    @RenderMapping
    public String view() {
    	
    	return "view";
    	
    }
    
    @ModelAttribute("form")
    public VisioForm getForm(PortletRequest request, PortletResponse response) throws PortletException {
    	try {
    		
        	PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
    		
			return service.getForm(pcc);
		} catch (VisioException e) {
			 VisioForm bean = context.getBean(VisioForm.class);
			 bean.setError(true);
			 return bean;
		}
    }

}
