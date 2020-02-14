package fr.gouv.education.foad.bns.batch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.ldap.LdapName;
import javax.portlet.PortletContext;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
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


	private Log log = LogFactory.getLog("org.osivia.directory.v2");
	
	private static PortletContext portletContext;
	
	private BnsImportForm form;

	private PersonUpdateService personService;
	
	private LdapName profile;
	
    /** regex for mails */
    private static final String MAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    /** Mail pattern. */
    private final Pattern mailPattern;
	private CSVPrinter rejectsPrinter;
	
	
	public BnsImportBatch(BnsImportForm form) {
		
		personService = DirServiceFactory.getService(PersonUpdateService.class);
		
		this.form = form;
		
		profile = LdapNameBuilder.newInstance(System.getProperty("ldap.base")).add("ou=groups").add("ou=profiles").add("cn="+form.getProfile().getProfileName()).build();

        // Mail pattern
        this.mailPattern = Pattern.compile(MAIL_REGEX);
	}
	
	@Override
	public String getJobScheduling() {
        // Start immediatly one shot
		return null;

	}

	@Override
	public void execute(Map<String, Object> parameters) throws PortalException {

		log.warn("Fire extraction of : "+form.getTemporaryFile().getAbsolutePath());
		
		
		
		// find all persons that have the given profile
		Person search = personService.getEmptyPerson();
		search.getProfiles().add(profile);
		List<Person> findByCriteria = personService.findByCriteria(search);
		
		// While query is returning results
		while(!findByCriteria.isEmpty()) {
			for(Person personToClean : findByCriteria) {
				
				// Remove the given profile
				
				log.info("Remove "+personToClean.getUid()+ " from "+form.getProfile().getProfileName());

				personToClean.getProfiles().remove(profile);
    	    	personService.update(personToClean);

			}
			findByCriteria = personService.findByCriteria(search);
			
		}
		
		
		try {
			CSVParser parser = CSVParser.parse(form.getTemporaryFile(), StandardCharsets.UTF_8, CSVFormat.EXCEL);
			boolean hasRejects = false;
			
			for(CSVRecord record : parser) {
				
				String uid = record.get(0);
				
	    		// minify uid
	    		uid = StringUtils.lowerCase(uid);
	    		// trim
	    		uid = uid.trim();
	    		
	    		if (StringUtils.isNotBlank(uid)) {
	    			
	    			Person person = personService.getPerson(uid);
	    			if(person != null) {
	    				
	        	    	// Apply profile
	    				log.info("Import "+record.get(0)+ " to "+form.getProfile().getProfileName());
	    				person.getProfiles().add(this.profile);
	        	    	personService.update(person);
	    				
	    			}
	    			else {
	    				
		                Matcher matcher = this.mailPattern.matcher(uid);
		                if (!matcher.matches()) {
		                	log.info("Reject "+record.get(0));

		    				rejectsPrinter = getRejectPrinter();
		    				rejectsPrinter.printRecord(uid);
		    				hasRejects = true;
		    				
		                }
		                else {
		                	log.info("Create "+record.get(0)+ " and add to "+form.getProfile().getProfileName());

	        	    		person = personService.getEmptyPerson();

    	    	    		person.setUid(uid);
    	    	    		person.setMail(uid);
    	    	    		person.setSn(uid);
    	    	    		person.setGivenName(uid);
    	    	    		person.setCn(uid);
    	    	    		person.setDisplayName(uid);
    	    				person.getProfiles().add(this.profile);

    	    	    		personService.create(person);
		                }
	    				
	    			}
	    		}
	    		    	    	
			}
			
			if(hasRejects) {
				rejectsPrinter.flush();
				rejectsPrinter.close();
			}
			
		} catch (IOException e) {
			throw new PortalException(e);
		}
		
		
	}
	
	/**
	 * Create a file for rejects if not exist
	 * @return
	 * @throws IOException
	 */
	private CSVPrinter getRejectPrinter() throws IOException {

		if(rejectsPrinter == null) {
			File rejects = new File(form.getTemporaryFile().getAbsolutePath() + "_rejects");
			rejects.createNewFile();
			
			rejectsPrinter = new CSVPrinter(new FileWriter(rejects), CSVFormat.EXCEL);
		}
		return rejectsPrinter;
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
