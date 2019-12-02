package fr.gouv.education.foad.bns.batch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.naming.ldap.LdapName;
import javax.portlet.PortletContext;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.ldap.support.LdapNameBuilder;

import fr.gouv.education.foad.bns.controller.BnsImportForm;
import fr.toutatice.portail.cms.nuxeo.api.batch.NuxeoBatch;

public class BnsImportBatch extends NuxeoBatch {

	private final static Log logger = LogFactory.getLog("batch");
	private static PortletContext portletContext;
	
	private BnsImportForm form;

	private PersonUpdateService personService;
	
	private LdapName profile;

	
	
	public BnsImportBatch(BnsImportForm form) {
		
		personService = DirServiceFactory.getService(PersonUpdateService.class);
		
		this.form = form;
		
		profile = LdapNameBuilder.newInstance(System.getProperty("ldap.base")).add("ou=groups").add("ou=profiles").add("cn="+form.getProfile().getProfileName()).build();
		
	}
	
	@Override
	public String getJobScheduling() {
        // Start immediatly one shot
		return null;

	}

	@Override
	public void execute(Map<String, Object> parameters) throws PortalException {

		logger.warn("Fire extraction of : "+form.getTemporaryFile().getAbsolutePath());
		
		// find all persons that have the given profile
		Person search = personService.getEmptyPerson();
		search.getProfiles().add(profile);
		List<Person> findByCriteria = personService.findByCriteria(search);
		
		// While query is returning results
		while(!findByCriteria.isEmpty()) {
			for(Person personToClean : findByCriteria) {
				
				// Remove the given profile
				
				logger.info("Remove "+personToClean.getUid()+ " from "+form.getProfile().getProfileName());

				personToClean.getProfiles().remove(profile);
    	    	personService.update(personToClean);

			}
			findByCriteria = personService.findByCriteria(search);
			
		}
		
		
		try {
			CSVParser parser = CSVParser.parse(form.getTemporaryFile(), StandardCharsets.UTF_8, CSVFormat.EXCEL);
			
			for(CSVRecord record : parser) {
				
				logger.info("Import "+record.get(0)+ " to "+form.getProfile().getProfileName());
				
				String uid = record.get(0);
    	    	Person person = personService.getPerson(uid);
    	    	
    	    	// if person is unknown, initialize it.
    	    	if(person == null) {
    	    		person = personService.getEmptyPerson();
    	    		person.setUid(uid);
    	    		person.setMail(uid);
    	    		person.setSn(uid);
    	    		person.setGivenName(uid);
    	    		person.setCn(uid);
    	    		person.setDisplayName(uid);
    	    		personService.create(person);
    	    	}
    	    	
    	    	// Apply profile
				person.getProfiles().add(this.profile);
    	    	personService.update(person);
    	    	
			}
			
			
		} catch (IOException e) {
			throw new PortalException(e);
		}
		
		
	}
	
	
	@Override
	public String getBatchId() {
		return form.getTemporaryFile().getName();
	}

	public void setPortletContext(PortletContext portletContext) {
		BnsImportBatch.portletContext = portletContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.toutatice.portail.cms.nuxeo.api.batch.FormBatch#getPortletContext()
	 */
	@Override
	protected PortletContext getPortletContext() {
		return BnsImportBatch.portletContext;
	}

}
