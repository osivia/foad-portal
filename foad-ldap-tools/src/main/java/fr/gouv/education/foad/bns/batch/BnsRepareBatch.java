package fr.gouv.education.foad.bns.batch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Name;
import javax.naming.ldap.LdapName;
import javax.portlet.PortletContext;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.model.Person;
import org.springframework.ldap.support.LdapNameBuilder;

import fr.gouv.education.foad.bns.controller.BnsImportForm;
import fr.gouv.education.foad.bns.controller.BnsRepareForm;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.batch.NuxeoBatch;

public class BnsRepareBatch extends NuxeoBatch {

	private final static Log logger = LogFactory.getLog("batch");
	private static PortletContext portletContext;
	
	private BnsRepareForm form;

	private PersonUpdateService personService;
	
	private WorkspaceService workspaceService;
	
	public BnsRepareBatch(BnsRepareForm form) {
		
		personService = DirServiceFactory.getService(PersonUpdateService.class);
		workspaceService = DirServiceFactory.getService(WorkspaceService.class);
		
		this.form = form;
		
	}
	
	@Override
	public String getJobScheduling() {
        // Start immediatly one shot
		return null;

	}

	@Override
	public void execute(Map<String, Object> parameters) throws PortalException {

		logger.warn("Fire extraction of : "+form.getTemporaryFile().getAbsolutePath());
				
		try {
			CSVParser parser = CSVParser.parse(form.getTemporaryFile(), StandardCharsets.UTF_8, CSVFormat.EXCEL);
			
			for(CSVRecord record : parser) {
				
				logger.info("Repare "+record.get(0));
				
				String uid = record.get(0);
    	    	Person person = personService.getPerson(uid);
    	    	
    	    	// if person is unknown, initialize it.
    	    	if(person == null) {

    				logger.info("uid "+uid+" not found");
    	    	}
    	    	else {
    	    		
    	    		String uidLowerCase = StringUtils.lowerCase(uid);
    	    		String uidInDirectory = person.getUid();
    	    		
    	    		if(!(uidLowerCase.equals(uidInDirectory))) {
    	    			
    	    			// === Remove nx profile
    	    			INuxeoCommand command = new GetUserProfileCommand(uid);
    	    			Document nxprofile = (Document) getNuxeoController().executeNuxeoCommand(command);
    					
    					int lastIndexOf = StringUtils.lastIndexOf(nxprofile.getPath(), "/");
    					String userworkspacePath = nxprofile.getPath().substring(0, lastIndexOf);
    	    			
    	    			INuxeoCommand removeCommand = new RemoveUserProfile(userworkspacePath);
    	    			getNuxeoController().executeNuxeoCommand(removeCommand);
    	    			
    	    			
    	    			// Remove collab profiles refs

    	    			CollabProfile collab = workspaceService.getEmptyProfile();
    	    			List<Name> uniqueMember = new ArrayList<Name>();
    	    			uniqueMember.add(person.getDn());
    	    			collab.setType(WorkspaceGroupType.security_group);
						collab.setUniqueMember(uniqueMember);
						List<CollabProfile> findByCriteria = workspaceService.findByCriteria(collab);
						
						for(CollabProfile profile : findByCriteria) {
							workspaceService.removeMember(profile.getWorkspaceId(), person.getDn());
						}
    	    			
    	    			// Remove user
    	    			personService.delete(person);
    	    			    	    			
    	    			
    	    			// Re-create user
    	    			person.setUid(uidLowerCase);
    	    			
    	    			personService.create(person);
    	    			
    	    			// Re-Apply profiles

						for(CollabProfile profile : findByCriteria) {
							workspaceService.addOrModifyMember(profile.getWorkspaceId(), person.getDn(), profile.getRole());
						}
						
						
    	    			
    	    		}
    	    		else {
        				logger.info("uid "+uid+" is alredy in lowercase");

    	    		}
    	    		
    	    	}
    	    	
    	    	
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
		BnsRepareBatch.portletContext = portletContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see fr.toutatice.portail.cms.nuxeo.api.batch.FormBatch#getPortletContext()
	 */
	@Override
	protected PortletContext getPortletContext() {
		return BnsRepareBatch.portletContext;
	}

}
