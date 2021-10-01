package fr.gouv.education.foad.generator.repository;

import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PathRef;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class ModifyWebIdCommand implements INuxeoCommand {

	private Document doc;
	private String webid;

	public ModifyWebIdCommand(Document doc, String webid) {
		this.doc = doc;
		this.webid = webid;
	}

	@Override
	public Object execute(Session nuxeoSession) throws Exception {

		DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
		documentService.setProperty(new PathRef(doc.getPath()), "ttc:webid", webid);
		
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
