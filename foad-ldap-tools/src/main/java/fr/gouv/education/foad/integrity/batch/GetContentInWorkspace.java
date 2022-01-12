package fr.gouv.education.foad.integrity.batch;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Get all contents (trashed or not) in a workspace
 * @author Loïc Billon
 *
 */
public class GetContentInWorkspace implements INuxeoCommand {

	private Document workspace;

	public GetContentInWorkspace(Document workspace) {
		this.workspace = workspace;
		
	}
	
	@Override
	public Object execute(Session nuxeoSession) throws Exception {
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set("query", "SELECT * FROM Document WHERE ecm:mixinType != 'Folderish' AND ecm:primaryType != 'Staple'" +
        		" AND ecm:path STARTSWITH '"+workspace.getPath()+"'");
        request.set("pageSize", "1");
        request.set("currentPageIndex", "0");
        request.set(Constants.HEADER_NX_SCHEMAS, "*");
        
        return request.execute();
        
	}

	@Override
	public String getId() {
		return null;
	}

	
}
