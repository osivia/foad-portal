package fr.gouv.education.foad.integrity.batch;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * 
 * Get workspaces in admin trash
 * @author Loïc Billon
 *
 */
public class GetWorkspacesInTrashCommand implements INuxeoCommand {

	public final static int PAGE_SIZE = 1000;
	
	private int currentPageIndex;

	private Integer delaiJoursEspaceEnCorbeille;

	private String path;
	
	public GetWorkspacesInTrashCommand(int currentPageIndex, Integer delaiJoursEspaceEnCorbeille, String path) {
		this.currentPageIndex = currentPageIndex;
		this.delaiJoursEspaceEnCorbeille = delaiJoursEspaceEnCorbeille;
		this.path = path;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#execute(org.nuxeo.ecm.automation.client.Session)
	 */
	@Override
	public Object execute(Session nuxeoSession) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
		Date referenceDate = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(referenceDate); 
		c.add(Calendar.DAY_OF_YEAR, Math.negateExact(delaiJoursEspaceEnCorbeille));
		referenceDate = c.getTime();
		
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set("query", "SELECT * FROM DOcument WHERE ecm:primaryType = 'Workspace' AND ecm:path STARTSWITH '"+path+"' " +
        		" AND dc:modified < DATE '"+sdf.format(referenceDate)+"'"+
        		" AND ecm:currentLifeCycleState = 'deleted'");
        request.set("pageSize", PAGE_SIZE);
        request.set("currentPageIndex", currentPageIndex);
        request.set(Constants.HEADER_NX_SCHEMAS, "*");
        
		return nuxeoSession.execute(request);
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#getId()
	 */
	@Override
	public String getId() {
		return this.getClass().getSimpleName() +
				" page:"+currentPageIndex + 
				" " + new Date().getTime();
	}

}