package fr.gouv.education.foad.integrity.batch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.portlet.PortletContext;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.nuxeo.ecm.automation.client.model.PaginableDocuments;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.directory.v2.DirServiceFactory;

import com.sun.mail.smtp.SMTPTransport;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.batch.NuxeoBatch;

/**
 * 
 * @author Loïc Billon
 *
 */
public class SupprEspacesVidesBatch extends NuxeoBatch {


	private static PortletContext portletContext;
	
	private Log log = LogFactory.getLog("batch");
	
	private WorkspaceService workspaceService;

	private Integer delaiJoursEspaceVide;

	private Integer delaiJoursEspaceSansVisite;

	private Integer delaiJoursEspaceEnCorbeille;

	private Boolean notification;

	private String path;

	private boolean testMode;
	
	public SupprEspacesVidesBatch() {
		workspaceService = DirServiceFactory.getService(WorkspaceService.class);
		
		String propDelaiJoursEspaceVide = System.getProperty("foad.purgeespaces.delaiJoursEspaceVide");
		if(StringUtils.isNotBlank(propDelaiJoursEspaceVide)) {
			delaiJoursEspaceVide = Integer.parseInt(propDelaiJoursEspaceVide);
		}
		else {
			delaiJoursEspaceVide = 60;
		}
		
		
		String propDelaiJoursEspaceSansVisite = System.getProperty("foad.purgeespaces.delaiJoursEspaceSansVisite");
		if(StringUtils.isNotBlank(propDelaiJoursEspaceSansVisite)) {
			delaiJoursEspaceSansVisite = Integer.parseInt(propDelaiJoursEspaceSansVisite);
		}
		else {
			delaiJoursEspaceSansVisite = 300;
		}
		
		String propDelaiJoursEspaceEnCorbeille = System.getProperty("foad.purgeespaces.delaiJoursEspaceEnCorbeille");
		if(StringUtils.isNotBlank(propDelaiJoursEspaceEnCorbeille)) {
			delaiJoursEspaceEnCorbeille = Integer.parseInt(propDelaiJoursEspaceEnCorbeille);
		}
		else {
			delaiJoursEspaceEnCorbeille = 60;
		}
		
		String propNotification = System.getProperty("foad.purgeespaces.notification");
		if(StringUtils.isNotBlank(propNotification)) {
			notification = BooleanUtils.toBoolean(propNotification);
		}
		else {
			notification = true;
		}
		
		
		String propTestmode = System.getProperty("foad.purgeespaces.testmode");
		if(StringUtils.isNotBlank(propTestmode)) {
			testMode = BooleanUtils.toBoolean(propTestmode);
		}
		else {
			testMode = false;
		}
		
		
		
		String propPath = System.getProperty("foad.purgeespaces.path");
		if(StringUtils.isNotBlank(propPath)) {
			path = propPath;
		}
		else {
			path = "/default-domain/workspaces/";
		}
	} 

	@Override
	public PortletContext getPortletContext() {
		return portletContext;
	}


	public static void setPortletContext(PortletContext portletContext) {
		SupprEspacesVidesBatch.portletContext = portletContext;
	}
	

	@Override
	public String getJobScheduling() {

		String enabled = System.getProperty("foad.purgeespaces.enabled");
		// la propriété doit être non vide et à false pour désactiver le traitement
		if (enabled != null && !BooleanUtils.toBoolean(enabled)) {
			return null;
		}
		
		String cron = System.getProperty("foad.purgeespaces.cron");
		
		if(StringUtils.isNotBlank(cron)) {
			return cron;
		}
		else return "0 0 9 ? * SAT";
		
		
	}
	
	@Override
	public void execute(Map<String, Object> parameters) throws PortalException {
		
		if(delaiJoursEspaceVide > 0) {
			supprEspacesVides();
		}
		
		if(delaiJoursEspaceSansVisite > 0) {
			corbeilleEspacesSansVisite();
		}
		
		if(delaiJoursEspaceEnCorbeille > 0) {
			vidageCorbeille();
		}

	}




	private void supprEspacesVides() {
		// Charge par pages de 1000.

		NuxeoController nuxeoController = getNuxeoController();

		int pageIndex = 0;
		int pageCount = 1;
		
		List<Document> spacesToRemove = new ArrayList<>();

		do {

			GetAllWorkspacesCommand command = new GetAllWorkspacesCommand(pageIndex, delaiJoursEspaceVide, path);
			PaginableDocuments workspaces = (PaginableDocuments) nuxeoController.executeNuxeoCommand(command);

			pageCount = workspaces.getPageCount();

			log.info("=== Suppression des espaces vides, page " + pageIndex + "/" + pageCount);

			for (Document workspace : workspaces.list()) {
				GetContentInWorkspace contentCmd = new GetContentInWorkspace(workspace);
				Documents documents = (Documents) nuxeoController.executeNuxeoCommand(contentCmd);

				if (documents.size() == 0) {
					log.info("L'Espace " + workspace.getTitle() + " est vide.");
					spacesToRemove.add(workspace);
				} 

			}
			pageIndex++;

		} while (pageIndex < pageCount);

		if(!testMode) {
			for (Document spaceToRemove : spacesToRemove) {
	
				// ==> desaffectation personne
				// ==> suppr. ldap
	
				String workspaceId = spaceToRemove.getString("webc:url");
	
				log.info("Suppression définitive de " + spaceToRemove.getTitle() + " (" + workspaceId + ")");
	
				workspaceService.delete(workspaceId);
	
				// ==> suppr. définitive nuxeo
				DeleteWorkspaceCommand deleteCommand = new DeleteWorkspaceCommand(spaceToRemove);
				nuxeoController.executeNuxeoCommand(deleteCommand);
				
			}
		}
		
		
	}
	
	
	private void corbeilleEspacesSansVisite() {
		
		// Charge par pages de 1000.

		NuxeoController nuxeoController = getNuxeoController();

		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
		
		int pageIndex = 0;
		int pageCount = 1;
		
		List<Document> spacesToPutInTrash = new ArrayList<>();
		
		do {

			GetWorkspacesNoVisitCommand command = new GetWorkspacesNoVisitCommand(pageIndex, delaiJoursEspaceSansVisite, path);
			PaginableDocuments workspaces = (PaginableDocuments) nuxeoController.executeNuxeoCommand(command);

			pageCount = workspaces.getPageCount();

			log.info("=== Mise en corbeille des espaces sans viste, page " + pageIndex + "/" + pageCount);

			for (Document workspace : workspaces.list()) {

				
				Date lastVisit = workspace.getDate("stats:lastUpdate");
				if(lastVisit == null) {
					lastVisit = workspace.getDate("dc:created");
				}
//									
//				if(lastVisit.before(referenceDate)) {
					log.info("L'espace " + workspace.getTitle() + " n'a pas été visite depuis le "+sdf.format(lastVisit));
					spacesToPutInTrash.add(workspace);					
					
//				}
			}
			pageIndex++;

		} while (pageIndex < pageCount);		
		
		
		StringBuilder reporting = new StringBuilder();
		 
		if(!testMode) {
			for (Document spaceToPutInTrash : spacesToPutInTrash) {
	
				log.info("Mise en corbeille de " + spaceToPutInTrash.getTitle());
				
				String workspaceId = spaceToPutInTrash.getString("webc:url");
				List<String> owners = new ArrayList<String>();
				List<WorkspaceMember> allMembers = workspaceService.getAllMembers(workspaceId);
				for(WorkspaceMember member : allMembers) {
					if(member.getRole() == WorkspaceRole.OWNER) {
						owners.add(member.getMember().getMail());
					}
				}
				
				// ==> suppr. définitive nuxeo
				PutWorkspaceInTrashCommand deleteCommand = new PutWorkspaceInTrashCommand(spaceToPutInTrash);
				nuxeoController.executeNuxeoCommand(deleteCommand);
	
				if(notification && owners.size() > 0) {
					// Envoi mail
					try {
						String to = String.join(";", owners);
						String object = "Désactivation de votre espace ".concat(spaceToPutInTrash.getTitle());
						
				        StringBuilder body = new StringBuilder();
				        body.append("<p>Suite à l’inactivité sur votre espace ");
				        body.append(spaceToPutInTrash.getTitle());
				        body.append(" depuis plus de 10 mois, cet espace a été mis en corbeille. </p>");
				        
				        body.append("<p>Si vous voulez récupérer les documents qui y sont déposés, contactez le Pôle FOAD ");
				        body.append("(<a href=\"mailto:pole.foad@ac-toulouse.fr\">pole.foad@ac-toulouse.fr</a>).</p>");
				        					
						sendMail(to, object, body);
						
						reporting.append("<li>");
						reporting.append(spaceToPutInTrash.getTitle());
						reporting.append(" (");
						reporting.append(owners);
						reporting.append(")");
						reporting.append("</li>");
						
						
					} catch (MessagingException e) {
						log.error("Impossible de notifier les propriétaires de l'espace " + spaceToPutInTrash.getTitle(), e);
		
					}
				}
				else {
					log.warn("Les propriétaires de l'espace " + spaceToPutInTrash.getTitle()+ " ne sont pas notifiés.");
	
				}
				
			}
			
			if(spacesToPutInTrash.size() > 0) {
				
				
				String to = "pole.foad@ac-toulouse.fr";
				String object = "Mise en corbeille d'espaces";
				
		        StringBuilder body = new StringBuilder();
		        body.append("<p>Suite à l’inactivité sur plusieurs espaces, ils ont été déplacé dans la corbeille administrateur et les propriétaires ont été notifiés.</p><ul>");
		        body.append(reporting);
		        body.append("</ul>");
	
		        try {
		        	sendMail(to, object, body);
		        
				} catch (MessagingException e) {
					log.error("Impossible de notifier les administrateurs du pôle",e);
		
				}
			}
		}
	}	
	
	public void sendMail(String mailToVar, String mailObjectVar, StringBuilder body) throws MessagingException {
		
        // Parameters
        String mailFromVar = System.getProperty("osivia.procedures.default.mail.from");
        String mailReplyToVar = "pole.foad@ac-toulouse.fr";
        

        // System properties
        Properties properties = System.getProperties();

				
		String subjectPrefix = properties.getProperty("mail.subject.prefix");
		if(StringUtils.isNotBlank(subjectPrefix)) {
			mailObjectVar = "[".concat(subjectPrefix).concat("] ").concat(mailObjectVar);
		}
		

		Authenticator auth = null;
		Session mailSession = Session.getInstance(properties, auth);


        // Message
        MimeMessage message = new MimeMessage(mailSession);

        // "Mail from" address
        InternetAddress mailFromAddr = null;
        if (StringUtils.isNotBlank(mailFromVar)) {
        	mailFromAddr = new InternetAddress(mailFromVar);
        }
        
        // "Mail reply-to" address
        InternetAddress[] mailReplyToAddr = null;
        if (StringUtils.isNotBlank(mailReplyToVar)) {
           	mailReplyToAddr = InternetAddress.parse(mailReplyToVar, false);
        }        
        
        // "Mail to" address
        InternetAddress[] mailToAddr;
        mailToAddr = InternetAddress.parse(mailToVar, false);

		
		
		 message.setFrom(mailFromAddr);
         message.setRecipients(Message.RecipientType.TO, mailToAddr);
         message.setSubject(mailObjectVar, "UTF-8");

         // Multipart
         Multipart multipart = new MimeMultipart();
         MimeBodyPart htmlPart = new MimeBodyPart();
         htmlPart.setContent(body.toString(), "text/html; charset=UTF-8");
         multipart.addBodyPart(htmlPart);
         message.setContent(multipart);

         message.setSentDate(new Date());

         // Reply-to
         if (mailFromAddr != null) {
         	if (mailReplyToAddr == null) {
        
	                InternetAddress[] replyToTab = new InternetAddress[1];
	                replyToTab[0] = mailFromAddr;
	                message.setReplyTo(replyToTab);
         	}
	            else {
	                message.setReplyTo(mailReplyToAddr);
	            }
         }
         
         // SMTP transport
         SMTPTransport transport = (SMTPTransport) mailSession.getTransport();
         transport.connect();
         transport.sendMessage(message, message.getAllRecipients());
         transport.close();
	}

	private void vidageCorbeille() {
		// Charge par pages de 1000.

		NuxeoController nuxeoController = getNuxeoController();

		int pageIndex = 0;
		int pageCount = 1;
//		Date referenceDate = new Date();
//		Calendar c = Calendar.getInstance(); 
//		c.setTime(referenceDate); 
//		c.add(Calendar.MONTH, Math.negateExact(delaiJoursEspaceEnCorbeille));
//		referenceDate = c.getTime();
				
		List<Document> spacesToRemove = new ArrayList<>();

		do {

			GetWorkspacesInTrashCommand command = new GetWorkspacesInTrashCommand(pageIndex, delaiJoursEspaceEnCorbeille, path);
			PaginableDocuments workspacesInTrash = (PaginableDocuments) nuxeoController.executeNuxeoCommand(command);

			pageCount = workspacesInTrash.getPageCount();

			log.info("=== Suppression des espaces mis en corbeille, page " + pageIndex + "/" + pageCount);

			for (Document workspaceInTrash : workspacesInTrash.list()) {

				spacesToRemove.add(workspaceInTrash);

			}
			pageIndex++;

		} while (pageIndex < pageCount);

		if(!testMode) {
			for (Document spaceToRemove : spacesToRemove) {
	
				// ==> desaffectation personne
				// ==> suppr. ldap
	
				String workspaceId = spaceToRemove.getString("webc:url");
	
				log.info("Suppression définitive de " + spaceToRemove.getTitle() + " (" + workspaceId + ")");
	
				workspaceService.delete(workspaceId);
	
				// ==> suppr. définitive nuxeo
				DeleteWorkspaceCommand deleteCommand = new DeleteWorkspaceCommand(spaceToRemove);
				nuxeoController.executeNuxeoCommand(deleteCommand);
				
	
			}
		}

	}
}

