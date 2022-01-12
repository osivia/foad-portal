package fr.gouv.education.foad.integrity.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.IdRef;
import org.nuxeo.ecm.automation.client.model.OperationInput;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Delete a workspace
 * 
 * @author Loïc Billon
 *
 */
public class DeleteWorkspaceCommand implements INuxeoCommand {

	private Log log = LogFactory.getLog("batch");
	
	private Document spaceToRemove;

	public DeleteWorkspaceCommand(Document spaceToRemove) {
		this.spaceToRemove = spaceToRemove;
	}

	@Override
	public Object execute(Session nuxeoSession) throws Exception {

		OperationRequest deleteCmd = nuxeoSession.newRequest("Services.PurgeDocuments");
		OperationInput input = new IdRef(spaceToRemove.getId());
		deleteCmd.setInput(input);
		deleteCmd.execute();
		
        
        return null;
	}

	@Override
	public String getId() {

		return null;
	}

}
