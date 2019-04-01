package fr.gouv.education.foad.room.controller;

import java.io.IOException;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.workspace.portlet.repository.command.GetPermissionsCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.gouv.education.foad.room.controller.RoomMigration.State;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
@SessionAttributes({ "form" })
public class RoomController extends CMSPortlet implements PortletConfigAware, PortletContextAware {

	private Log log = LogFactory.getLog("org.osivia.directory.v2");

	
    /** Portlet config. */
    private PortletConfig portletConfig;
    /** Portlet context. */
    private PortletContext portletContext;
    
    /** Bundle factory. */
    @Autowired
    protected IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    protected INotificationsService notificationsService;    

    @Autowired
    protected ApplicationContext applicationContext;
    
    /** Portal URL factory. */
    @Autowired
    @Qualifier("urlFactoryForRoomMig")
    private IPortalUrlFactory portalUrlFactory;    
    
    /**
     * Constructor.
     */
    public RoomController() {
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
     * @throws IOException 
     */
    @ActionMapping(value = "migration")
    public void migration(ActionRequest request, ActionResponse response, @ModelAttribute("form") RoomMigForm form) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(this.portletContext, request, response);
        
        // Current window
        PortalWindow window = WindowFactory.getWindow(request);
        // Path
        String path = window.getProperty(Constants.WINDOW_PROP_URI);
        
        NuxeoController nuxeoController = new NuxeoController(request, response, portletContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        
        
        boolean runExec = BooleanUtils.toBoolean(request.getParameter("exec"));
        
        if(!runExec) {
        	
            log.info("===========");
            log.info("Début analyse,");
        	
            nuxeoController.setAsynchronousCommand(false);
            
	        // Extraction des dossiers à migrer
	        List<RoomMigration> lrm = (List<RoomMigration>) nuxeoController.executeNuxeoCommand(new AnalyzeRoomsCommand(path, form));
	        form.setLrm(lrm);
	
	        // Extraction des ACLs
	        examineAcl(nuxeoController, form);
	        
	        form.setAnalyseDone(true);
	        
            log.info("Fin analyse,");
            log.info("===========");
        }
        else {
        	
        	nuxeoController.setAsynchronousCommand(true);        			
        	nuxeoController.executeNuxeoCommand(new TransformRoomCommand(form.getLrm()));
        	
        	Bundle bundle = this.bundleFactory.getBundle(request.getLocale());
        	
            this.notificationsService.addSimpleNotification(portalControllerContext, bundle.getString("MIGRATION_IN_PROGRESS"), NotificationsType.INFO);

        	String cmsUrl = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, null, null, IPortalUrlFactory.DISPLAYCTX_REFRESH, null, null, null, null);
            response.sendRedirect(cmsUrl);


        }
        	
        
    }





	private void examineAcl(NuxeoController nuxeoController, RoomMigForm form) {
		log.info("= Phase 2 - recherche des droits à extraire.");
        
		Integer nbRoomsInError = form.getNbRoomsInError();
		
        for(RoomMigration rm : form.getLrm()) {
        	
        	INuxeoCommand command;
        	
        	// Vérification, la salle contient des ACLs à préserver
        	
        	command = this.applicationContext.getBean(GetPermissionsCommand.class, rm.getRoom());
            JSONObject result = (JSONObject) nuxeoController.executeNuxeoCommand(command);
            JSONArray localArray = result.getJSONArray("local");
            if(localArray.size() > 0) {
            
                log.warn(" La salle "+rm.getRoom().getTitle()+" possède des droits spécifiques qui seront migrés..");

            	rm.setAcls(localArray);
            }
        }
        
        form.setNbRoomsInError(nbRoomsInError);
	}
    
    /**
     * 
     * @return
     */
    @ModelAttribute("form")
    public RoomMigForm getChgValidDateForm() {
    	return new RoomMigForm();
    	
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
