package fr.gouv.education.foad.integrity.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.Name;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * Service implementation.
 *
 * @author Loïc Billon
 * @see IntegrityService
 */
@Service
public class IntegrityServiceImpl implements IntegrityService {

	private Log log = LogFactory.getLog("INTEGRITY");
	
	@Autowired
	private WorkspaceService wsService;
	
	@Autowired
	private PersonService personService;
	
	/* (non-Javadoc)
	 * @see fr.gouv.education.foad.integrity.service.IntegrityService#checkIntegrity(org.osivia.portal.api.context.PortalControllerContext)
	 */
	@Override
	public void checkIntegrity(PortalControllerContext portalControllerContext, boolean repare)
			throws PortletException {
		
		// Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        nuxeoController.setAsynchronousCommand(false);
		
		// Recherche des groupes membre
		CollabProfile profile = wsService.getEmptyProfile();
		profile.setType(WorkspaceGroupType.space_group);
		List<CollabProfile> workspaces = wsService.findByCriteria(profile);
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		// Pour tous les groups trouvés (= espaces)
		for(CollabProfile workspace : workspaces) {
			
			String workspaceId = workspace.getWorkspaceId();
			
			// Recherche document nx de workspace
			Document workspaceDoc = (Document) nuxeoController.executeNuxeoCommand(new GetWorkspaceCommand(workspaceId));
			try {
				if(workspaceDoc != null) {
					PropertyList map = workspaceDoc.getProperties().getList("ttcs:spaceMembers");
				
					int membersNx = 0;

					int membersLdap = workspace.getUniqueMember().size();
					if(map != null) {
						membersNx = map.size();
					}
					String title = workspaceDoc.getString("dc:title");
					
					// Si le nombre de membres est différents
					if(membersLdap > membersNx) {
											
						Date created = workspaceDoc.getDate("dc:created");
						log.error(title +" ("+workspaceId+") créé le "+sdf.format(created)+" compte "+membersNx+" membres au lieu de "+membersLdap);
						
						// Mémorisation de tous les logins
						List<String> memberLogins = new ArrayList<String>();
						for(Object element : map.list()) {
							PropertyMap pmap = (PropertyMap) element;
							memberLogins.add(pmap.getString("login"));
						}
						
						for(Name uniqueMember : workspace.getUniqueMember()) {
							String uid = uniqueMember.get(uniqueMember.size() -1);
							uid = uid.replace("uid=", "");
							log.error(uid +" est manquant et sera ajouté à l'espace "+title);

							if(!memberLogins.contains(uid) && repare) {
								nuxeoController.executeNuxeoCommand(new UpdateWorkspaceCommand(workspaceId, uid, true));
							}

						}

					}
					//else {
						//log.info(title+ " a été vérifé. Ok.");
					//}
					
				}else {
					log.error("Workspace "+workspaceId+" introuvable dans Nuxeo !");
				}
			}catch(Exception e) {
				log.error("Exception sur le workspace "+workspaceId, e);
			}
				
		}
		
	}
	
	/**
     * Get Nuxeo controller
     *
     * @param portalControllerContext portal controller context
     * @return Nuxeo controller
     */
    private NuxeoController getNuxeoController(PortalControllerContext portalControllerContext) {
        PortletRequest request = portalControllerContext.getRequest();
        PortletResponse response = portalControllerContext.getResponse();
        PortletContext portletContext = portalControllerContext.getPortletCtx();
        return new NuxeoController(request, response, portletContext);
    }




}
