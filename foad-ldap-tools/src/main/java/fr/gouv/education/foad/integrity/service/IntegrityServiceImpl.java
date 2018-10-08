package fr.gouv.education.foad.integrity.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PropertyList;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.workspace.portlet.model.Invitation;
import org.osivia.services.workspace.portlet.repository.MemberManagementRepository;
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

	private Log log = LogFactory.getLog("org.osivia.directory.v2");
	
	@Autowired
	private WorkspaceService wsService;
	
	@Autowired
	private PersonUpdateService personService;
	
	@Autowired
	private MemberManagementRepository memberRepo;
	
	@Autowired
	private IBundleFactory bundleFactory;
	
	@Autowired
	private INotificationsService notificationService;
	
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

    /* (non-Javadoc)
	 * @see fr.gouv.education.foad.integrity.service.IntegrityService#purgeUsers(org.osivia.portal.api.context.PortalControllerContext, boolean)
	 */
	@Override
	public void purgeAllUsers(PortalControllerContext portalControllerContext, Boolean test) throws PortletException {
		
		Person search = personService.getEmptyPerson();
		//search.setExternal(true);

		List<Person> persons = personService.findByNoConnectionDate(search);
		
		List<String> logins = new ArrayList<>();
		for(Person p : persons) {
			logins.add(p.getUid());
		}
		
		int count = purgeUsers(portalControllerContext, logins , test);
		
		while(!test && count > 0) {
			persons = personService.findByNoConnectionDate(search);
			
			logins = new ArrayList<>();
			for(Person p : persons) {
				logins.add(p.getUid());
			}
			
			count = purgeUsers(portalControllerContext, logins , test);
		}
		
		
	}
	
    
	/* (non-Javadoc)
	 * @see fr.gouv.education.foad.integrity.service.IntegrityService#purgeUsers(org.osivia.portal.api.context.PortalControllerContext, boolean)
	 */
	@Override
	public int purgeUsers(PortalControllerContext portalControllerContext, List<String> logins, Boolean test) throws PortletException {

		int count = 0;
		int errCount = 0;
		NuxeoController controller = new NuxeoController(portalControllerContext);
		Date referenceDate = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(referenceDate); 
		c.add(Calendar.MONTH, -2);
		referenceDate = c.getTime();
		
		for(String login : logins) {
			try {
				Person p = personService.getEmptyPerson();
				Name dn = p.buildDn(login);
				p = personService.getPersonNoCache(dn);
				
				if(p == null) {
					log.warn("User "+login+ " not found");
					errCount++;
				}
				else if(p.getLastConnection() != null) {
					log.warn("User "+p.getUid()+ " has a connection date "+p.getLastConnection());
					errCount++;
				}
				else {
					boolean recentlyInvited = false;
					
					Documents invitations = (Documents) controller.executeNuxeoCommand(
							new GetProceduresInstancesCommand("invitation", "invitation", p.getUid()));
					
					if(invitations.size() > 0) {
						
						List<Invitation> form = new ArrayList<Invitation>();
						for(Document invitation : invitations) {
							
							Date modified = invitation.getDate("dc:modified");
							PropertyMap variables = invitation.getProperties().getMap("pi:globalVariablesValues");
							String workspaceId = variables.get("workspaceId").toString();
							
							if(modified.before(referenceDate)) {
								Invitation invit = new Invitation(p);
								invit.setDocument(invitation);
								invit.setDeleted(true);
								
								form.add(invit);
								
								log.info("Remove invitation for "+p.getUid()+" on "+workspaceId);
	
							}
							else {
								log.warn("Cannot remove invitation for "+p.getUid()+" on "+workspaceId+ " (recently modified)");
								recentlyInvited = true;
								errCount++;
							}
							
						}
						if(!recentlyInvited && !test) {
							memberRepo.updateInvitations(portalControllerContext, form);
						}
					}
					if(!recentlyInvited && !test) {
						personService.delete(p);
						count++;
					}
					
				}
			}
			catch(Exception e) {
				log.warn("Remove "+login+" failed, caused by : "+e.getMessage());
				errCount++;
			}
		}
		

		log.info(count+" account(s) deleted. "+errCount+" account(s) skipped. Change done.");
		
		Bundle bundle = bundleFactory.getBundle(null);
		notificationService.addSimpleNotification(portalControllerContext, bundle.getString("PURGE_LOGINS_STATUS", count, errCount), NotificationsType.ERROR);

		return count;
	}

	/* (non-Javadoc)
	 * @see fr.gouv.education.foad.integrity.service.IntegrityService#chgValidDate(java.util.Date, java.lang.Boolean)
	 */
	@Override
	public void chgValidDate(PortalControllerContext portalControllerContext, Date validity, Date current, Boolean validityTest) {

		int count = 0;
		int errCount = 0;	
		
		Person search = personService.getEmptyPerson();
		search.setValidity(current);;
		
		List<Person> persons = personService.findByValidityDate(current);
		for(Person person : persons) {
			
			if(person.getUid().endsWith("@tribu.local")) {

				person.setValidity(validity);
				
				log.info("Set validity date on "+person.getUid()+" to "+validity.toString());
				
				if(!validityTest) {
					personService.update(person);
				}
				count++;
			}
			else {
				log.warn(person.getUid() +" is not valid. Validity date not set");

			}
			
		}
		log.info(count+" account(s) modified. Change done.");
		
		Bundle bundle = bundleFactory.getBundle(null);
		notificationService.addSimpleNotification(portalControllerContext, bundle.getString("VALIDITY_MODIF_OK", count, errCount), NotificationsType.SUCCESS);
		
		
	}

	/* (non-Javadoc)
	 * @see fr.gouv.education.foad.integrity.service.IntegrityService#chgValidDate(java.util.Date, java.util.List, java.lang.Boolean)
	 */
	@Override
	public void chgValidDate(PortalControllerContext portalControllerContext, Date validity, List<String> logins, Boolean validityTest) {
		
		int count = 0;
		int errCount = 0;			
		
		for(String login : logins) {
			Person person = personService.getEmptyPerson();
			Name dn = person.buildDn(login);
			person = personService.getPersonNoCache(dn);
			
			if(person != null && person.getUid().endsWith("@tribu.local")) {
				log.info("Current validity date on "+login+" is "+person.getValidity().toString());

				person.setValidity(validity);
				
				log.info("Set validity date on "+login+" to "+validity.toString());
				
				if(!validityTest) {
					personService.update(person);
				}
				count++;
			}
			else {
				log.warn(login +" is not valid. Validity date not set");
				errCount++;

			}
		}
		log.info(count+" account(s) modified. Change done.");

		Bundle bundle = bundleFactory.getBundle(null);
		notificationService.addSimpleNotification(portalControllerContext, bundle.getString("VALIDITY_MODIF_OK", count, errCount), NotificationsType.SUCCESS);

	}

	/* (non-Javadoc)
	 * @see fr.gouv.education.foad.integrity.service.IntegrityService#purgeInvit(org.osivia.portal.api.context.PortalControllerContext, boolean)
	 */
	@Override
	public Integer purgeInvit(PortalControllerContext portalControllerContext, boolean test) throws PortletException {

		Integer count = 0;
		NuxeoController controller = new NuxeoController(portalControllerContext);
		Date referenceDate = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(referenceDate); 
		c.add(Calendar.MONTH, -2);
		referenceDate = c.getTime();
		
		Documents invitations = (Documents) controller.executeNuxeoCommand(
				new GetProceduresInstancesCommand("invitation", "invitation", referenceDate));
		
		if(invitations.size() > 0) {
			
			List<Invitation> form = new ArrayList<Invitation>();
			for(Document invitation : invitations) {
				
				Date modified = invitation.getDate("dc:modified");
				PropertyMap variables = invitation.getProperties().getMap("pi:globalVariablesValues");
				String workspaceId = variables.get("workspaceId").toString();
				String uid = variables.get("uid").toString();
				
				Invitation invit = new Invitation(uid);
				invit.setDocument(invitation);
				invit.setDeleted(true);
				
				form.add(invit);
				count++;
				log.info("Remove invitation for "+uid+" on "+workspaceId+ " created on "+modified);
				
			}
			if(!test) {
				memberRepo.updateInvitations(portalControllerContext, form);
			}
		}
		
		log.info(count+" invitations removed.");

		return count;
		
	}

	/* (non-Javadoc)
	 * @see fr.gouv.education.foad.integrity.service.IntegrityService#purgeAllInvit(org.osivia.portal.api.context.PortalControllerContext)
	 */
	@Override
	public void purgeAllInvit(PortalControllerContext portalControllerContext) throws PortletException {
		
		Integer totalPurgeCount = 0;
		Integer purgeCount = purgeInvit(portalControllerContext, Boolean.FALSE);
		totalPurgeCount = totalPurgeCount + purgeCount;
		
		while(purgeCount == Integer.parseInt(GetProceduresInstancesCommand.PAGE_SIZE)) {
			purgeCount = purgeInvit(portalControllerContext, Boolean.FALSE);
			totalPurgeCount = totalPurgeCount + purgeCount;
		}
		
		log.info(totalPurgeCount+" invitations removed. Change done.");

		
		Bundle bundle = bundleFactory.getBundle(null);
		notificationService.addSimpleNotification(portalControllerContext, bundle.getString("PURGE_INVITS_STATUS", totalPurgeCount), NotificationsType.SUCCESS);

	}



}
