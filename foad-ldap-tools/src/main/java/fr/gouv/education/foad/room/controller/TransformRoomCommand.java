/**
 * 
 */
package fr.gouv.education.foad.room.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentSecurityService;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.DocumentPermissions;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.services.workspace.portlet.model.Permission;

import fr.gouv.education.foad.room.controller.RoomMigration.State;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Transformation effective de toutes les salles identifiées
 * 
 * @author Loïc Billon
 *
 */
public class TransformRoomCommand implements INuxeoCommand {

	
	private Log log = LogFactory.getLog("org.osivia.directory.v2");

	
	private List<RoomMigration> lrm;

	/**
	 * @param rm
	 */
	public TransformRoomCommand(List<RoomMigration> lrm) {
		this.lrm = lrm;
	}

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#execute(org.nuxeo.ecm.automation.client.Session)
	 */
	@Override
	public Object execute(Session nuxeoSession) throws Exception {

        log.info("===========");
        log.info("Début migration,");
        
        Integer nbRoomsSkipped = 0, nbRoomsDone = 0;
		
        Collections.sort(lrm);
        
		for(RoomMigration rm : lrm) {
			
			log.info("Déplacement de "+rm.getRoom().getTitle()+")");

	        // Operation request
	        OperationRequest request = nuxeoSession.newRequest("Document.Query");
	        request.set("query", "SELECT * FROM Document WHERE ecm:uuid <> '"+rm.getRoom().getId()+"' AND ecm:primaryType = 'Room' AND ecm:path STARTSWITH '"+rm.getRoom().getPath()+"' "+AnalyzeRoomsCommand.FILTER_NOT_IN_TRASH);

	        Documents innerRooms = (Documents) request.execute();
	        
	        // Si la salle contient encore des sous-salles, pas de migration
	        if(innerRooms.size() > 0) {
	        	
	    		log.error(" Cette salle contient encore des sous-salles.");
	        	
	        	rm.setState(State.SKIP);
	        	nbRoomsSkipped++;
	        }
	        else if(rm.getState() != State.NEW) {
	        	
	    		log.error(" Cette salle avait été marquée en erreur et ne sera pas traitée.");
	        	
	        	rm.setState(State.SKIP);
	        	nbRoomsSkipped++;
	        }
	        else {
		        // Document service
		        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
		        
		        // Mémorisation et desaffectation du webid de la salle
		        String webid = rm.getRoom().getProperties().getString("ttc:webid");
				documentService.setProperty(rm.getRoom(), "ttc:webid", webid + "_old");
		        
				Document parent = documentService.getParent(rm.getRoom());
				
				request = nuxeoSession.newRequest("Document.QueryES");
		        request.set("query", "SELECT * FROM Document WHERE ecm:primaryType IN ('Folder','Note') AND ecm:parentId = '"+rm.getId()+"' "+AnalyzeRoomsCommand.FILTER_NOT_IN_TRASH);
		        Documents rootFolders = (Documents) request.execute();				
				
				// Déplacement des dossiers documents
				Document targetFolder = null;
				if(rootFolders.isEmpty()) {
					log.info(" Création d'un dossier vide "+rm.getRoom().getTitle());
	
					targetFolder = documentService.createDocument(parent, "Folder",webid);
				}
				else {
					
					log.info(" Regroupement des documents dans un dossier "+rm.getRoom().getTitle());
	
					targetFolder = documentService.createDocument(parent, "Folder", webid);
	
					for(Document folder : rootFolders) {
						
						log.info("  Document "+folder.getTitle());
	
						documentService.move(folder, targetFolder);
					}
				}
				// Déplacement des contenus divers
				for(Document misc : rm.getMiscDocs()) {
					log.info(" Déplacment de "+misc.getTitle()+" vers l'espace parent ");
					documentService.move(misc, parent);
				}
				
				documentService.setProperty(targetFolder, "ttc:webid", webid);
				documentService.setProperty(targetFolder, "dc:title", rm.getRoom().getTitle());
	
	
				rm.setTargetFolder(targetFolder);
				
				
				if(rm.getLocalArray() != null && rm.getState() == State.NEW) {
					
					transformAcls(nuxeoSession, rm);
				}
				
				
				log.info(" Suppression de l'ancienne salle "+rm.getRoom().getTitle());
	
				nuxeoSession.newRequest("Document.PutDocumentInTrash").set("document", rm.getRoom().getId()).setHeader("nx_es_sync", "true").execute();
				
				nbRoomsDone++;
	        }
		}
		
        log.info("Fin migration,");
        log.info("Salles traitées : "+nbRoomsDone+", salles non traitées : "+nbRoomsSkipped);
        log.info("===========");
        
		return null;

	}
	
	private void transformAcls(Session nuxeoSession, RoomMigration rm) throws Exception {
		
		log.info("Traitement des ACLs");
		
		// Permissions
		JSONArray localArray = rm.getLocalArray();
		Map<String, Permission> permissions = new HashMap<>(localArray.size());
		
		
		boolean publicEntry = false;
		boolean blockInherit = false;
		for (int i = 0; i < localArray.size(); i++) {
		    JSONObject object = localArray.getJSONObject(i);

		    if (object.getBoolean("isGranted")) {
		        // Name
		        String name = object.getString("username");

		        if (Permission.PUBLIC_NAME.equals(name)) {
		            publicEntry = true;
		        } else {
		            // Group indicator
		            boolean group = object.getBoolean("isGroup");

		            // Permission
		            Permission permission = permissions.get(name);
		            if (permission == null) {
		                permission = new Permission();
		                permission.setName(name);
		                permission.setValues(new ArrayList<String>());
		                permission.setGroup(group);
		                permissions.put(name, permission);
		            }
		            permission.getValues().add(object.getString("permission"));
		        }
		    } else {
		    	blockInherit = true;
		    }
		}
		
		List<Permission> permissionsList = new ArrayList<>(permissions.values());
		
		DocumentPermissions dtoPerms = toDocumentPermissions(permissionsList);
		
		 // Document Service
        DocumentSecurityService securityService = nuxeoSession.getAdapter(DocumentSecurityService.class);
        
        securityService.addPermissions(rm.getTargetFolder(), dtoPerms, DocumentSecurityService.LOCAL_ACL,
        		blockInherit);
        
        // Apply perms to other documents moved up
        for(Document misc : rm.getMiscDocs()) {
            securityService.addPermissions(misc, dtoPerms, DocumentSecurityService.LOCAL_ACL,
            		blockInherit);
            
        }
		
	}	
	
    private DocumentPermissions toDocumentPermissions(List<Permission> permissions) {
        DocumentPermissions documentPermissions;
        if (CollectionUtils.isEmpty(permissions)) {
            documentPermissions = new DocumentPermissions(0);
        } else {
            documentPermissions = new DocumentPermissions(permissions.size());

            for (Permission permission : permissions) {
                documentPermissions.setPermissions(permission.getName(), permission.getValues());
            }
        }
        return documentPermissions;
    }

	/* (non-Javadoc)
	 * @see fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand#getId()
	 */
	@Override
	public String getId() {
		return this.toString();
	}

}
