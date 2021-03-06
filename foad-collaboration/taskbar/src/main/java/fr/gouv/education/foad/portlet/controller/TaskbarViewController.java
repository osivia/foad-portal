package fr.gouv.education.foad.portlet.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import fr.gouv.education.foad.portlet.model.Taskbar;
import fr.gouv.education.foad.portlet.service.TaskbarService;
import net.sf.json.JSONArray;

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
     * Drop action mapping.
     * 
     * @param request action request
     * @param response action response
     * @param sourceIds source identifiers request parameter
     * @param targetId target identifier request parameter
     * @throws PortletException
     */
    @ActionMapping("drop")
    public void drop(ActionRequest request, ActionResponse response, @RequestParam("sourceIds") String sourceIds, @RequestParam("targetId") String targetId)
            throws PortletException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        this.service.drop(portalControllerContext, Arrays.asList(StringUtils.split(sourceIds, ",")), targetId);
    }


    /**
     * Lazy loading resource mapping.
     * 
     * @param request resource request
     * @param response resource response
     * @param path parent folder path request parameter
     * @throws PortletException
     * @throws IOException
     */
    @ResourceMapping("lazy-loading")
    public void lazyLoading(ResourceRequest request, ResourceResponse response, @RequestParam("path") String path) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);

        // JSON
        JSONArray array = this.service.getFolderChildren(portalControllerContext, path);

        // Content type
        response.setContentType("application/json");

        // Content
        PrintWriter printWriter = new PrintWriter(response.getPortletOutputStream());
        printWriter.write(array.toString());
        printWriter.close();
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
