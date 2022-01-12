package fr.gouv.education.foad.integrity.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Put a workspace in trash
 * 
 * @author Loïc Billon
 *
 */
public class PutWorkspaceInTrashCommand implements INuxeoCommand {

	private Log log = LogFactory.getLog("batch");
	
	private Document spaceToRemove;

	public PutWorkspaceInTrashCommand(Document spaceToRemove) {
		this.spaceToRemove = spaceToRemove;
	}

	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		
		OperationRequest deleteCmd = nuxeoSession.newRequest("Document.PutDocumentInTrash");

		deleteCmd.set("document", this.spaceToRemove.getPath());
		deleteCmd.execute();
        
        return null;
	}

	@Override
	public String getId() {

		return null;
	}

}
