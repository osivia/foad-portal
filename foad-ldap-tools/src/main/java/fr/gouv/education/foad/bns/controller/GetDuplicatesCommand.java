package fr.gouv.education.foad.bns.controller;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;


/**
 * Get Duplicates documnts
 * 
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetDuplicatesCommand implements INuxeoCommand {

	private Log log = LogFactory.getLog("org.osivia.directory.v2");
	
	/** Document path */
	private final String path;

	/**
	 * Constructor.
	 * 
	 * @param basePath
	 *            
	 */
	
    
	public GetDuplicatesCommand(String path) {
		super();
		this.path = path;
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object execute(Session nuxeoSession) throws Exception {
        

        DocRef workspace = new PathRef(path);
		OperationRequest request = nuxeoSession.newRequest("Document.GetDuplicateFiles");
		request.setInput(workspace);

		Object quota = request.execute();

		return quota;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getId() {
		StringBuilder builder = new StringBuilder();
		builder.append(this.getClass().getName());
		builder.append("/");
		builder.append(this.path);
		return builder.toString();
	}

}
