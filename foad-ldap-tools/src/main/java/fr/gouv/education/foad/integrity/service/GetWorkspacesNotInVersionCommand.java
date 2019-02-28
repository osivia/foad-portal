/**
 * 
 */
package fr.gouv.education.foad.integrity.service;

import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Documents;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * @author loic
 *
 */
public class GetWorkspacesNotInVersionCommand implements INuxeoCommand {

	private String workspaceVersion;
	
	
	public GetWorkspacesNotInVersionCommand(String workspaceVersion) {
		this.workspaceVersion = workspaceVersion;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#execute(org.nuxeo.ecm.automation.client.Session)
	 */
	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set("query", "SELECT * FROM Document WHERE (ecm:primaryType = 'Workspace' OR ecm:primaryType = 'Room') AND ecm:path STARTSWITH '/default-domain/workspaces' AND ttc:modelVersion <> '"+workspaceVersion+"' ");
        //request.setHeader(Constants.HEADER_NX_SCHEMAS, "dublincore, toutatice, toutatice_space, webcontainer");
        request.set("pageSize", 1000);
        request.set("currentPageIndex", 0);
        
		Documents workspaces = (Documents) nuxeoSession.execute(request);

		return workspaces;
		
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#getId()
	 */
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
