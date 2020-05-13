package fr.gouv.education.foad.bns.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.IdRef;
import org.nuxeo.ecm.automation.client.model.PathRef;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

public class RepareDuplicateCommand implements INuxeoCommand {

	private Log log = LogFactory.getLog("org.osivia.directory.v2");
	
	private String path;

	public RepareDuplicateCommand(String path) {
		this.path = path;
	}

	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		
		OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set("query", "SELECT * FROM Document WHERE ecm:path ='"+path+"' "
        		+ " AND ecm:isCheckedInVersion = 0 AND ecm:currentLifeCycleState != 'deleted'");
        Documents duplicates = (Documents) request.execute();
        
        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        
        for(Document duplicate : duplicates) {
        	try {
	        	log.warn("Repare "+path+" ("+duplicate.getId()+")");
	        	
	        	Document parent = documentService.getParent(new PathRef(path));
	        	
	        	String originalTitle = duplicate.getTitle();
	        	Document copy = documentService.copy(new IdRef(duplicate.getId()), parent);
	        	log.warn("Relocate copy on "+copy.getId() + " with new path "+copy.getPath());
	        	
	        	documentService.remove(new IdRef(duplicate.getId()));
	        	documentService.setProperty(copy, "dc:title", originalTitle);
        	}
        	catch(Exception e) {
	        	log.error("Error when reparing "+path);

        	}
        }
        
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

}
