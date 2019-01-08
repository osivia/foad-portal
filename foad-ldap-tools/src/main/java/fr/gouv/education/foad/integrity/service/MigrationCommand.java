/**
 * 
 */
package fr.gouv.education.foad.integrity.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.DocRef;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.portal.api.taskbar.ITaskbarService;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * @author loic
 *
 */
public class MigrationCommand implements INuxeoCommand {

	private Log log = LogFactory.getLog("org.osivia.directory.v2");

	
	private Document workspace;

	/**
	 * @param workspace
	 */
	public MigrationCommand(Document workspace) {
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
        
        String pageTemplate = workspace.getProperties().getString("ttc:pageTemplate");
		if(!"/default/templates/workspace-simple".equals(pageTemplate)) {
	        createLastModifStaple(documentService);
		}
        
        migratePictureBooks(documentService);
        
		log.info("Migration / execute - "+workspace.getTitle() + " ("+workspace.getPath()+")");
        
        documentService.setProperty(workspace, "ttc:pageTemplate", null);
        documentService.setProperty(workspace, "ttc:modelVersion", "4.4.16");


		
		return null;
	}

	private void migratePictureBooks(DocumentService documentService) throws Exception {

		Documents pictureBooks = documentService.query("SELECT * FROM PictureBook WHERE ecm:path STARTSWITH '"+workspace.getPath()+"'");
        List<DocRef> pbooksToRemove = new ArrayList<>();
        
        for(Document pictureBook : pictureBooks) {

        	Documents pictures = documentService.query("SELECT * FROM Picture WHERE ecm:path STARTSWITH '"+pictureBook.getPath()+"' ");
        	
        	if(pictures.size() > 0) {
        		
       		
        		// si picturebook enfant de workspace ou room, création d'un folder au même niveau, déplacement de toutes les images dedans
        		int lastIndexOf = pictureBook.getPath().lastIndexOf("/");
        		String parentPath = pictureBook.getPath().substring(0, lastIndexOf);

        		Document parent = documentService.getDocument(parentPath);
        		
        		if(parent.getType().equals("Workspace") || parent.getType().equals("Room")) {
        			
            		log.info("Migration / migratePictureBooks - "+pictureBook.getTitle() + " ("+pictureBook.getPath()+")");

            		
            		String pbookWebid = pictureBook.getProperties().getString("ttc:webid");
            		documentService.setProperty(pictureBook, "ttc:webid", pbookWebid + "_old");
            		
            		// Properties
            		PropertyMap properties = new PropertyMap();
            		properties.set("dc:title", pictureBook.getTitle());
            		properties.set("ttc:showInMenu", true);
            		properties.set("ttc:webid", pbookWebid);

            		Document folderDest = documentService.createDocument(parent, "Folder", pbookWebid, properties);
            		
            		for(Document picture : pictures) {
            			documentService.move(picture, folderDest);
            		}
            		
 
        		}
        		else {
            		log.warn("Migration / skip sub-picturebook - "+pictureBook.getTitle() + " ("+pictureBook.getPath()+")");

        		}
        	}
        	else {
        		log.warn("Migration / skip empty picturebook - "+pictureBook.getTitle() + " ("+pictureBook.getPath()+")");

        	}
        	
           	// Picturebook vide, suppression
        	pbooksToRemove.add(pictureBook);

        }
        
        for(DocRef pbookToRemove : pbooksToRemove) {
        	documentService.remove(pbookToRemove);
        }
	}

	private void createLastModifStaple(DocumentService documentService) throws Exception {
		
		
		// Workspace shortname
		String shortname = workspace.getString("webc:url");
		// WebId prefix
		String webIdPrefix = ITaskbarService.WEBID_PREFIX + shortname + "_";
		
		String type = "Staple";
		
		String webId = webIdPrefix + StringUtils.lowerCase("last_modifications");
		
		Documents query = documentService.query("SELECT * FROM Document WHERE ttc:webid = '"+webId+"'");
		
		if(query.size() == 0) {
			log.info("Migration / createLastModifStaple - "+workspace.getTitle() + " ("+workspace.getPath()+")");

			// Properties
			PropertyMap properties = new PropertyMap();
			properties.set("dc:title", "Dernières modifications");
			properties.set("ttc:showInMenu", true);
			properties.set("ttc:webid", webId);
	
			documentService.createDocument(workspace, type, "dernieres-modifications", properties);
		}
		else {
			log.warn("Migration / createLastModifStaple - "+workspace.getTitle() + " ("+workspace.getPath()+") already exists.");

		}
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
