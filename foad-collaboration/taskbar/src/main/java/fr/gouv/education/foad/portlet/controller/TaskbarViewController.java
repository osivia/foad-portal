package fr.gouv.education.foad.portlet.controller;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import fr.gouv.education.foad.portlet.model.Taskbar;
import fr.gouv.education.foad.portlet.service.TaskbarService;

/**
 * Taskbar portlet view controller.
 * 
 * @author Cédric Krommenhoek
 */
@Controller
@RequestMapping("VIEW")
public class TaskbarViewController {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Portlet service. */
    @Autowired
    private TaskbarService service;


    /**
     * Constructor.
     */
    public TaskbarViewController() {
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
     * Get taskbar model attribute.
     * 
     * @param request portlet request
     * @param response portlet response
     * @return taskbar
     * @throws PortletException
     */
    @ModelAttribute("taskbar")
    public Taskbar getTaskbar(PortletRequest request, PortletResponse response) throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
        
        return this.service.getTaskbar(portalControllerContext);
    }

}
