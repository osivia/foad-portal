package fr.gouv.education.foad.bns.batch;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.naming.Name;
import javax.portlet.PortletContext;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
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
import org.osivia.portal.api.directory.v2.DirServiceFactory;
import org.osivia.portal.api.directory.v2.model.Person;

import fr.gouv.education.foad.bns.controller.BnsRepareForm;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.batch.NuxeoBatch;

public class BnsRepareBatch extends NuxeoBatch {

	private final static Log logger = LogFactory.getLog("org.osivia.directory.v2");
	private static PortletContext portletContext;
	
	private BnsRepareForm form;

	private PersonUpdateService personService;
	
	private WorkspaceService workspaceService;
	
	private CSVPrinter rejectsPrinter;	
	
	public BnsRepareBatch(BnsRepareForm form) {
		
		super(form.getTemporaryFile().getName());
		
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
			boolean hasRejects = false;

			int count = 1;
			for(CSVRecord record : parser) {
				
				String uid = record.get(0); // skip blank lines
				
				if(StringUtils.isNotBlank(uid)) {
					
					logger.info("Repare "+record.get(0)+ " line "+count);

	    	    	Person person = personService.getPerson(uid);
	    	    	
	    	    	// if person is unknown, initialize it.
	    	    	if(person == null) {

	    				logger.info("uid "+uid+" not found");
	    	    	}
	    	    	else {
	    	    		
	    	    		String uidLowerCase = StringUtils.lowerCase(uid);
	    	    		String uidInDirectory = person.getUid();
	    	    		
	    	    		if(!(uidLowerCase.equals(uidInDirectory))) {
	    	    			
	    	    			try {
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
	    	    			catch(Exception e) {
	    	    				logger.info("Rejected  "+record.get(0));

			    				rejectsPrinter = getRejectPrinter();
			    				rejectsPrinter.printRecord(uid);
			    				hasRejects = true;
	    	    			}
							
	    	    			
	    	    		}
	    	    		else {
	        				logger.info("uid "+uid+" is alredy in lowercase");

	    	    		}
	    	    		
	    	    	}
				}
				
    	    	count++;

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
