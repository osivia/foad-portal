package fr.gouv.education.foad.integrity.batch;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.IdRef;
import org.nuxeo.ecm.automation.client.model.OperationInput;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class DeleteProfileDocumentCommand implements INuxeoCommand {


	private Log log = LogFactory.getLog("batch");
	
	private final String uid;
	
	public DeleteProfileDocumentCommand(String uid) {
		this.uid = uid;
	}

	@Override
	public Object execute(Session nuxeoSession) throws Exception {
        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "*");
        request.set("query", "SELECT * FROM UserProfile WHERE ttc_userprofile:login = '"+uid+"'");
        
        Documents userprofiles = (Documents) request.execute();
        
        log.info(userprofiles.size()+" profil(s) trouvé(s) dans nuxeo pour "+uid);
        
        for(Document userprofile : userprofiles) {
        	String parentpath = StringUtils.substringBeforeLast(userprofile.getPath(), "/");
        	
        	if(StringUtils.isNotBlank(parentpath)) {
        		request.set("query", "SELECT * FROM Workspace WHERE ecm:path STARTSWITH '"+parentpath+"'");
        		Documents userworkspaces = (Documents) request.execute();
        		
                for(Document userworkspace : userworkspaces) {
	        		OperationRequest deleteCmd = nuxeoSession.newRequest("Services.PurgeDocuments");
	        		OperationInput input = new IdRef(userworkspace.getId());
					deleteCmd.setInput(input);
					deleteCmd.execute();
					
	                log.info("Suppression espace "+userworkspace.getId()+" pour "+uid);

                }
        		
        	}
        	
        	
        	
        }
        return userprofiles.size();
	}

	@Override
	public String getId() {

		return null;
	}

}
