package fr.gouv.education.foad.integrity.batch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.model.Person;

import fr.gouv.education.foad.directory.model.TribuPerson;
import fr.gouv.education.foad.integrity.controller.SupprNumenForm;
import fr.toutatice.portail.cms.nuxeo.api.batch.NuxeoBatch;

public class SupprNumenBatch extends NuxeoBatch {

	private static PortletContext portletContext;
	
	private Log log = LogFactory.getLog("batch");
	
	private SupprNumenForm form;

	private PersonUpdateService personService;
	
	public SupprNumenBatch(SupprNumenForm form) {
		super(form.getTemporaryFile().getName());
		personService = DirServiceFactory.getService(PersonUpdateService.class);
		this.form = form;
	}



	@Override
	public String getJobScheduling() {
		// No scheduling
		return null;
	}

	@Override
	public void execute(Map<String, Object> parameters) throws PortalException {
		
		
		try {
			CSVParser parser;
			parser = CSVParser.parse(form.getTemporaryFile(), StandardCharsets.UTF_8, CSVFormat.EXCEL);
			
			for(CSVRecord record : parser) {
				
				String numen = record.get(0).trim();
				
					if(StringUtils.isNotBlank(numen)) {
					TribuPerson search = (TribuPerson) personService.getEmptyPerson();
					search.setHashNumen(numen);
					List<Person> findByCriteria = personService.findByCriteria(search);
					
					if(findByCriteria.size() == 0) {
						log.warn("Aucune personne trouvée pour le NUMEN "+numen);
	
					}if(findByCriteria.size() > 1) {
						log.warn("Plusieurs comptes trouvés avec le NUMEN "+numen);
	
					}
					
					for(Person p : findByCriteria) {
						TribuPerson person = (TribuPerson) p;
						
						person.setHashNumen(null);
						personService.update(person);
						
						log.info("Suppresion du NUMEN "+numen+" pour "+person.getUid());
					}
				
				}
			}
		} catch (IOException e) {
			throw new PortalException(e);
		}
		

	}


	@Override
	public PortletContext getPortletContext() {
		return portletContext;
	}


	public static void setPortletContext(PortletContext portletContext) {
		SupprNumenBatch.portletContext = portletContext;
	}

	
}
