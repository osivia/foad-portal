 /**
 * 
 */
package fr.gouv.education.foad.room.controller;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;

import fr.gouv.education.foad.room.controller.RoomMigration.State;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Analyser les documents d'une salle
 * @author Loïc Billon
 *
 */
public class AnalyzeRoomsCommand implements INuxeoCommand {


	private Log log = LogFactory.getLog("org.osivia.directory.v2");
	
	/**
	 * 
	 */
	public static final String FILTER_NOT_IN_TRASH = " AND ecm:isCheckedInVersion = 0 AND ecm:currentLifeCycleState != 'deleted'";
	
	
	private String wsPath;

	private RoomMigForm form;
	
	public AnalyzeRoomsCommand(String wsPath, RoomMigForm form) {
		this.wsPath = wsPath;
		this.form = form;
	}
	
	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#execute(org.nuxeo.ecm.automation.client.Session)
	 */
	@Override
	public Object execute(Session nuxeoSession) throws Exception {
		
        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set("query", "SELECT * FROM Document WHERE ecm:primaryType = 'Room' AND ecm:path STARTSWITH '"+wsPath+"' "+FILTER_NOT_IN_TRASH);
        request.set("pageSize", 1000);
        request.set("currentPageIndex", 0);

        Documents rooms = (Documents) request.execute();
        
        log.info(rooms.size()+" salle(s) trouvée(s).");
        
        List<RoomMigration> lrm = new ArrayList<RoomMigration>();
        Integer maxLevelOfPath = 0;
        Integer nbRooms = 0;
        
        for(Document room : rooms) {
        	
        	String[] split = room.getPath().split("/");
        	int levelOfPath = split.length;
        	
        	if(levelOfPath > maxLevelOfPath) {
        		maxLevelOfPath = levelOfPath;
        	}
        	
        	String id = room.getId();
        	
        	RoomMigration rm = new RoomMigration();
        	rm.setId(id);
        	rm.setLevelOfPath(levelOfPath);
        	rm.setDocument(room);
        	lrm.add(rm);
        	
        	nbRooms++;
        }
        
        form.setNbRooms(nbRooms);
        
        testRoom(nuxeoSession, lrm);
        
        return lrm;
        
	}

	/**
	 * @param lrm
	 * @throws Exception 
	 */
	private void testRoom(Session nuxeoSession, List<RoomMigration> lrm) throws Exception {
		
        log.info("= Phase 1 - recherche des dossiers à extraire.");

        Integer nbRoomsInError = 0;

		for(RoomMigration rm : lrm) {
			
	        log.info("Analyse de la salle  "+rm.getRoom().getTitle()+" . Niveau : "+rm.getLevelOfPath());

			
			// test du contenu de la salle
			//  Contient 1 ou des dossiers document
			OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
	        request.set("query", "SELECT * FROM Document WHERE ecm:primaryType IN ('Folder','Note') AND ecm:parentId = '"+rm.getId()+"' "+FILTER_NOT_IN_TRASH);
	        Documents rootFolders = (Documents) request.execute();
	        
	        if(rootFolders.size() == 0) {
	        	rm.setEmptyFolder(true);
		        log.info(" La salle ne contient pas de dossier document, un dossier vide sera créé.");

	        }
	        else {
	        	rm.setRootFolders(rootFolders);
	        	
	        	for(Document rootFolder : rootFolders) {
			        log.info(" La salle contient "+rootFolder.getTitle()+ " ("+rootFolder.getType()+") à migrer.");

	        	}
	        	
	        }

			
			// Ne contient pas autre chose
			request = nuxeoSession.newRequest("Document.QueryES");
	        request.set("query", "SELECT * FROM Document WHERE ecm:primaryType NOT IN ( 'File','Folder','Audio','Video','Note','Staple','Agenda', 'VEVENT') AND ttc:spaceID = '"+rm.getId()+"' "+FILTER_NOT_IN_TRASH);
	        Documents docsToWarn = (Documents) request.execute();
	        
	        if(docsToWarn.size() > 0) {
	        
		    	for(Document docToWarn : docsToWarn) {
			        log.info(" La salle contient un document "+docToWarn.getTitle()+ " ("+docToWarn.getType()+") qui sera perdu");
		    	}
		    	rm.setState(State.ERROR);
		        nbRoomsInError++;

	        }
				
		}
		
		form.setNbRoomsInError(nbRoomsInError);
		
		
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
