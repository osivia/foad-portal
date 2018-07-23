/**
 * 
 */
package fr.gouv.education.foad.integrity.service;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Documents;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * @author Loïc Billon
 *
 */
public class GetWorkspaceCommand implements INuxeoCommand {

	
	private String workspaceId;
	
	/**
	 * 
	 */
	public GetWorkspaceCommand(String workspaceId) {
		
		this.workspaceId = workspaceId;
	}
	
	
	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#execute(org.nuxeo.ecm.automation.client.Session)
	 */
	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		
		OperationRequest request = nuxeoSession.newRequest("Document.Query");
        request.set("query", "SELECT * FROM Workspace WHERE webc:url = '"+workspaceId+"'");
        request.setHeader(Constants.HEADER_NX_SCHEMAS, "dublincore, toutatice_space");
		
        
		Documents workspaces = (Documents) nuxeoSession.execute(request);
		if(workspaces.size() > 0)
			return workspaces.get(0);
		else return null;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#getId()
	 */
	@Override
	public String getId() {
		return this.getClass().getSimpleName() + "/"+workspaceId;
	}

}
