package fr.gouv.education.foad.integrity.batch;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.model.Person;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.batch.NuxeoBatch;

/**
 * Batch for clean ldap directory and nuxeo userworkspaces
 * 
 * @author Loïc Billon
 *
 */
public class SupprUtilisateursNonCoBatch extends NuxeoBatch {

	private static PortletContext portletContext;
	
	private Log log = LogFactory.getLog("batch");
	

	private PersonUpdateService personService;
	
	public SupprUtilisateursNonCoBatch() {
		personService = DirServiceFactory.getService(PersonUpdateService.class);

	}
	

	@Override
	public PortletContext getPortletContext() {
		return portletContext;
	}


	public static void setPortletContext(PortletContext portletContext) {
		SupprUtilisateursNonCoBatch.portletContext = portletContext;
	}

	@Override
	public String getJobScheduling() {
		
		String cron = System.getProperty("foad.purgeusers.cron");
		
		if(StringUtils.isNotBlank(cron)) {
			return cron;
		}
		else return "0 0/15 * * * ?";
	}


	@Override
	public void execute(Map<String, Object> parameters) throws PortalException {
		
		
		Date referenceDate = new Date();
		Calendar c = Calendar.getInstance(); 
		c.setTime(referenceDate); 
		c.add(Calendar.MONTH, -2);
		referenceDate = c.getTime();
		
		List<Person> persons = personService.findByNoConnectionDate(referenceDate);
		int supprLdap = 0, supprNx = 0;
		
		if(persons != null) {
			for(Person person : persons) {
				log.info("Suppression de "+person.getUid()+ " (compte sans date de connexion)");
				
				INuxeoCommand command = new DeleteProfileDocumentCommand(person.getUid());
				Integer ret = (Integer) getNuxeoController().executeNuxeoCommand(command);
				supprNx += ret;
				
				personService.delete(person);
				
				supprLdap++;
			}
		}
		
		List<Person> inactivePersons = personService.findInactives(referenceDate);
		
		if(inactivePersons != null) {
			for(Person person : inactivePersons) {
				log.info("Suppression de "+person.getUid()+ " (compte sans activité depuis 2 mois)");
				
				INuxeoCommand command = new DeleteProfileDocumentCommand(person.getUid());
				Integer ret = (Integer) getNuxeoController().executeNuxeoCommand(command);
				supprNx += ret;
				
				personService.delete(person);
				
				supprLdap++;
			}
		}
		
		log.info("Total suppression ldap "+supprLdap+" et nuxeo "+supprNx);
	}

}
