/**
 * 
 */
package fr.gouv.education.foad.integrity.service;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.IdRef;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * @author loic
 *
 */
public class DeleteDocumentCommand implements INuxeoCommand {

	
	private String uuid;

	public DeleteDocumentCommand(String uuid) {
		this.uuid = uuid;
		
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#execute(org.nuxeo.ecm.automation.client.Session)
	 */
	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		
        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        documentService.remove(new IdRef(uuid));
        
		
		return null;
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
