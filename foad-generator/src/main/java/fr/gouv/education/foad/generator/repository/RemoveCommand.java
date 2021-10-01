package fr.gouv.education.foad.generator.repository;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class RemoveCommand implements INuxeoCommand {

	private Document doc;

	public RemoveCommand(Document doc) {
		this.doc = doc;
	}

	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		

        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        documentService.remove(doc.getPath());
        
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
