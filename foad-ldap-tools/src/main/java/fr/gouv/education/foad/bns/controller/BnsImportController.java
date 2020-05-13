package fr.gouv.education.foad.bns.controller;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

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

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.batch.IBatchService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.gouv.education.foad.bns.batch.BnsImportBatch;
import fr.gouv.education.foad.bns.batch.BnsRepareBatch;
import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
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
public class BnsImportController extends CMSPortlet implements PortletConfigAware, PortletContextAware {

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
        

	@Autowired
	private IBatchService batchService;


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
     * @throws PortalException 
     * @throws ParseException 
     */
    @ActionMapping(value = "executeImport")
    public void executeImport(ActionRequest request, ActionResponse response, @ModelAttribute("form") BnsImportForm form) throws PortletException, IOException, ParseException, PortalException {
        
    	// Temporary file
        MultipartFile upload = form.getFile().getUpload();
        File temporaryFile = File.createTempFile("bns-", ".tmp");
        temporaryFile.deleteOnExit();
        upload.transferTo(temporaryFile);
        form.setTemporaryFile(temporaryFile);
        
        // Prepare batch
        BnsImportBatch batch = new BnsImportBatch(form);
        batch.setPortletContext(portletContext);
        batchService.addBatch(batch);
        
        PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
		getNotificationsService().addSimpleNotification(pcc, "Batch programmé", NotificationsType.SUCCESS);

        
    }
    
    @ActionMapping(value = "detectDuplicate")
    public void detectDuplicate(ActionRequest request, ActionResponse response, @ModelAttribute("form") BnsImportForm form) throws PortalException {
    	
    	PortalControllerContext pcc = new PortalControllerContext(getPortletContext(), request, response);
		NuxeoController controller = new NuxeoController(pcc);
		
		INuxeoCommand command = new GetDuplicatesCommand(form.getDuplicatePath());
		Blob json = (Blob) controller.executeNuxeoCommand(command);


		try {
			// get current quota
			if (json != null) {
				
				log.warn("directory : "+form.getDuplicatePath() + (form.isTestOnly() ? " (test only)" : ""));


				String jsonStr;

				jsonStr = IOUtils.toString(json.getStream(), "UTF-8");

				//log.warn(jsonStr);
				
				JSONObject duplicates = JSONObject.fromObject(jsonStr);
				JSONArray docs = duplicates.getJSONArray("docs");
				
				for (int i = 0; i < docs.size(); i++) {
				    JSONObject jsonobject = docs.getJSONObject(i);
				    String path = jsonobject.getString("path");
				    String count = jsonobject.getString("count");
				    
					log.warn(i+ " "+ path+ " => "+count);
					
					if(!form.isTestOnly()) {
						controller.executeNuxeoCommand(new RepareDuplicateCommand(path));
					}
				}
				
			}
		
		} catch (IOException e) {
			throw new PortalException(e);
		} 
		
    }

    
    /**
     * 
     * @return
     */
    @ModelAttribute("form")
    public BnsImportForm getForm() {
    	BnsImportForm form = new BnsImportForm();
    	form.setDuplicatePath("/default-domain/workspaces/bnsujet");
    	return form;
    	
    }
    
    /**
     * 
     * @return
     */
    @ModelAttribute("repareform")
    public BnsRepareForm getRepareForm() {
    	BnsRepareForm form = new BnsRepareForm();
    	return form;
    	
    }



    /**
     * 
     *
     * @param request action request
     * @param response action response
     * @throws PortletException
     * @throws IOException 
     * @throws PortalException 
     * @throws ParseException 
     */
    @ActionMapping(value = "repareAccounts")
    public void repareAccounts(ActionRequest request, ActionResponse response, @ModelAttribute("repareform") BnsRepareForm form) throws PortletException, IOException, ParseException, PortalException {
        
    	// Temporary file
        MultipartFile upload = form.getFile().getUpload();
        File temporaryFile = File.createTempFile("bnsrepare-", ".tmp");
        temporaryFile.deleteOnExit();
        upload.transferTo(temporaryFile);
        form.setTemporaryFile(temporaryFile);
        
        // Prepare batch
        BnsRepareBatch batch = new BnsRepareBatch(form);
        batch.setPortletContext(portletContext);
        batchService.addBatch(batch);
        
        PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
		getNotificationsService().addSimpleNotification(pcc, "Batch programmé", NotificationsType.SUCCESS);

        
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
