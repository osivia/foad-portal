/**
 * 
 */
package fr.gouv.education.foad.integrity.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * @author loic
 *
 */
public class MigrationInErrorCommand implements INuxeoCommand {

	private Log log = LogFactory.getLog("org.osivia.directory.v2");

	
	private Document workspace;

	/**
	 * @param workspace
	 */
	public MigrationInErrorCommand(Document workspace) {
		this.workspace = workspace;
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#execute(org.nuxeo.ecm.automation.client.Session)
	 */
	@Override
	public Object execute(Session nuxeoSession) throws Exception {

        // Document service
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        
        documentService.setProperty(workspace, "ttc:modelVersion", "err");

		
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
