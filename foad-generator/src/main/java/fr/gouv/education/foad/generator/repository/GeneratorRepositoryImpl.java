package fr.gouv.education.foad.generator.repository;

import io.codearte.jfairy.Fairy;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import javax.naming.NamingException;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cache.services.CacheInfo;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.services.workspace.edition.portlet.model.WorkspaceEditionForm;
import org.osivia.services.workspace.edition.portlet.service.WorkspaceEditionService;
import org.osivia.services.workspace.portlet.model.WorkspaceCreationForm;
import org.osivia.services.workspace.portlet.service.WorkspaceCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.gouv.education.foad.generator.model.Configuration;
import fr.gouv.education.foad.generator.model.GenerateForm;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.services.NuxeoCommandContext;

/**
 * Generator repository implementation.
 *
 * @author Cédric Krommenhoek
 * @see GeneratorRepository
 */
@Repository
public class GeneratorRepositoryImpl implements GeneratorRepository {

	private static Logger LOGGER = Logger.getLogger(GeneratorRepositoryImpl.class);
	
    /** Generator properties file name. */
    private static final String PROPERTIES_FILE_NAME = "generator.properties";
	/** Number of spaces. */
	private static final String NB_WORKSPACES_PROPERTY = "generator.nbOfworkspaces";
	
	private static final String NB_USERS = "generator.nbOfUsersPerWks";
	
	private static final String NB_FOLDERS = "generator.nbOfRootFolers";
	private static final String NB_SUBFOLDERS = "generator.nbOfSubFolers";
	private static final String NB_SUBITEMS = "generator.nbOfSubItems";
	


    /** Generator properties. */
    private final Properties properties;
    @Autowired
    private PersonUpdateService personService;
    
    @Autowired
    private WorkspaceService workspaceService;
    
    @Autowired
    private WorkspaceCreationService workspaceCreationService;
    
    
    /**
     * Constructor.
     *
     * @throws IOException
     * @throws NamingException
     */
    public GeneratorRepositoryImpl() throws IOException, NamingException {
        super();

        // Generator properties
        this.properties = new Properties();
        InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);
        if (inputStream != null) {
            this.properties.load(inputStream);
        } else {
            throw new FileNotFoundException(PROPERTIES_FILE_NAME);
        }


    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Configuration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        //int nbOfworkspaces = NumberUtils.toInt(StringUtils.defaultIfEmpty(window.getProperty(NB_WORKSPACES_PROPERTY), "10");
	      int nbOfworkspaces = NumberUtils.toInt(StringUtils.defaultIfEmpty(window.getProperty(NB_WORKSPACES_PROPERTY),
	    		  this.properties.getProperty(NB_WORKSPACES_PROPERTY)));
        
	      int nbOfUsers = NumberUtils.toInt(StringUtils.defaultIfEmpty(window.getProperty(NB_USERS),
	    		  this.properties.getProperty(NB_USERS)));
	      

	      int nbOfRootFolers = NumberUtils.toInt(StringUtils.defaultIfEmpty(window.getProperty(NB_FOLDERS),
	    		  this.properties.getProperty(NB_FOLDERS)));

	      int nbOfSubFolers = NumberUtils.toInt(StringUtils.defaultIfEmpty(window.getProperty(NB_SUBFOLDERS),
	    		  this.properties.getProperty(NB_SUBFOLDERS)));

	      int nbOfSubItems = NumberUtils.toInt(StringUtils.defaultIfEmpty(window.getProperty(NB_SUBITEMS),
	    		  this.properties.getProperty(NB_SUBITEMS)));
        
        // Configuration
        Configuration configuration = new Configuration();
        configuration.setNbOfworkspaces(nbOfworkspaces);
        configuration.setNbOfUsersPerWks(nbOfUsers);
        configuration.setNbOfRootFolers(nbOfRootFolers);
        configuration.setNbOfSubFolers(nbOfSubFolers);
        configuration.setNbOfSubItems(nbOfSubItems);


        return configuration;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setConfiguration(PortalControllerContext portalControllerContext, Configuration configuration) throws PortletException {
    	
      if (configuration.getNbOfworkspaces() == null) {
    	  configuration.setNbOfworkspaces(NumberUtils.toInt(this.properties.getProperty(NB_WORKSPACES_PROPERTY)));
      
      }
      if (configuration.getNbOfUsersPerWks() == null) {
    	  configuration.setNbOfUsersPerWks(NumberUtils.toInt(this.properties.getProperty(NB_USERS)));
      
      }
      
      if (configuration.getNbOfRootFolers() == null) {
    	  configuration.setNbOfRootFolers(NumberUtils.toInt(this.properties.getProperty(NB_FOLDERS)));
      
      }
      if (configuration.getNbOfSubFolers() == null) {
    	  configuration.setNbOfSubFolers(NumberUtils.toInt(this.properties.getProperty(NB_SUBFOLDERS)));
      
      }
      if (configuration.getNbOfSubItems() == null) {
    	  configuration.setNbOfSubItems(NumberUtils.toInt(this.properties.getProperty(NB_SUBITEMS)));
      
      }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void generate(PortalControllerContext portalControllerContext, GenerateForm generateForm) throws PortletException {
    	
    	URL exampleFile = this.getClass().getResource("/WEB-INF/classes/example.doc");
    	
        // Configuration
        Configuration configuration = this.getConfiguration(portalControllerContext);

        // Nuxeo controller
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        nuxeoController.setAsynchronousCommand(false);

        Locale locale = nuxeoController.getRequest().getLocale();

        Fairy fairy = Fairy.create(locale);
        
        int min = 1;
        if(generateForm.getMinIndex() != null) {
        	min = generateForm.getMinIndex();
        }
        
        int max = configuration.getNbOfworkspaces();
        if(generateForm.getMaxIndex() != null) {
        	max = generateForm.getMaxIndex();
        }
        
        for(int i = min; i <= max; i++) {
        	
        	LOGGER.info("creating space  "+Integer.toString(i));
        	
        	Person owner = createUser(portalControllerContext, fairy, i, 1);
	        
        	String workspaceId = "espace-tmc-" + Integer.toString(i);
        	WorkspaceCreationForm form = new WorkspaceCreationForm();
        	form.setTitle(workspaceId);
        	form.setDescription(workspaceId);
        	form.setOwner(owner.getUid());
        	
			workspaceCreationService.create(portalControllerContext, form);
			
			// Fix webid
			NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext("/default-domain/workspaces/espace-tmc-" + Integer.toString(i));
        	
        	if(documentContext.getDoc() != null) {
        		
        		Document doc = documentContext.getDoc();
        		INuxeoCommand command = new ModifyWebIdCommand(doc, workspaceId);
    			nuxeoController.executeNuxeoCommand(command);
        	}
			
			WorkspaceRole[] roles = WorkspaceRole.values();
			int rolesSize = roles.length;
			Random random = new Random();
			
			for(int j = 2; j <= configuration.getNbOfUsersPerWks(); j++) {
				Person createUser = createUser(portalControllerContext, fairy, i, j);
				
				LOGGER.info("Adding user  "+createUser.getCn()+ " in " +workspaceId);
				
				workspaceService.addOrModifyMember(workspaceId, createUser.getDn(), roles[random.nextInt(rolesSize)]);
			}
			
//        }
//        
//        for(int i = 1; i <= configuration.getNbOfworkspaces(); i++) {
	        NuxeoController nuxeoControllerAsync = this.getNuxeoController(portalControllerContext);
	        nuxeoControllerAsync.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
	        nuxeoControllerAsync.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
	        nuxeoControllerAsync.setAsynchronousCommand(true);
	        nuxeoControllerAsync.executeNuxeoCommand(new GenerateCommand(configuration, "/default-domain/workspaces/espace-tmc-" + Integer.toString(i) + "/documents", Integer.toString(i), fairy, exampleFile));
        }
        
    }


	private Person createUser(PortalControllerContext portalControllerContext, Fairy fairy, int i, int j) {
		

		LOGGER.debug("creating user  "+Integer.toString(i) + "-" + Integer.toString(j));
		
		Person owner = personService.getEmptyPerson();
		String uid = "utilisateur-" + Integer.toString(i) + "-" + Integer.toString(j)+ "@example.org";
		owner.setUid(uid);
		owner.setMail(uid);
		owner.setCreationDate(new Date());
		owner.setLastConnection(new Date());
		
		io.codearte.jfairy.producer.person.Person personGen = fairy.person();
		owner.setSn(personGen.firstName());
		owner.setGivenName(personGen.lastName());
		owner.setDisplayName(personGen.firstName() + " " + personGen.lastName());
		owner.setCn(personGen.lastName() + " " + personGen.firstName());
		if(personGen.isMale()) {
			owner.setTitle("M.");
		}
		else owner.setTitle("Mme.");
		
    	personService.create(owner);
    	personService.updatePassword(owner, "tmc");
    	
    	Map<String, String> nxproperties = new HashMap<String, String>();
    	nxproperties.put("ttc_userprofile:terms_of_use_agreement", "1");
		try {
			personService.update(portalControllerContext, owner, null, nxproperties );
		} catch (PortalException e) {
			LOGGER.error("error cgu");
		}
    	
    	return owner;
    	
	}


    /**
     * {@inheritDoc}
     */
    @Override
    public void purge(PortalControllerContext portalControllerContext, GenerateForm generateForm) throws PortletException {
    	
        // Configuration
        Configuration configuration = this.getConfiguration(portalControllerContext);
        
        
        NuxeoController nuxeoController = this.getNuxeoController(portalControllerContext);
        nuxeoController.setAuthType(NuxeoCommandContext.AUTH_TYPE_SUPERUSER);
        nuxeoController.setCacheType(CacheInfo.CACHE_SCOPE_NONE);
        nuxeoController.setAsynchronousCommand(false);
        
        
        int min = 1;
        if(generateForm.getMinIndex() != null) {
        	min = generateForm.getMinIndex();
        }
        
        int max = configuration.getNbOfworkspaces();
        if(generateForm.getMaxIndex() != null) {
        	max = generateForm.getMaxIndex();
        }
        
        for(int i = min; i <= max; i++) {
        	
        	
        	try {
        		LOGGER.info("deleting space  "+Integer.toString(i));
        		NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext("/default-domain/workspaces/espace-tmc-" + Integer.toString(i));
        	
	        	if(documentContext.getDoc() != null) {
	        		
	        		Document doc = documentContext.getDoc();
	        		String workspaceId = doc.getProperties().getString("webc:url");
	        		
	        		workspaceService.delete(workspaceId);
	        		
	        		INuxeoCommand command = new RemoveCommand(doc);
					nuxeoController.executeNuxeoCommand(command);
	        	}
        	}
        	catch(NuxeoException e) {
        		LOGGER.error("error deleting space number " + Integer.toString(i));
        	}
        	
            Person personsToDeleteQuery = personService.getEmptyPerson();
        	String uid = "utilisateur-"+i+"-*";
    		personsToDeleteQuery.setUid(uid);
            
    		
    		
    		List<Person> personsToDelete = personService.findByCriteria(personsToDeleteQuery);
    		
    		while(!personsToDelete.isEmpty()) {
    		
    			for(Person  p : personsToDelete) {
    				
    				LOGGER.warn("deleting "+p.getUid());
    				personService.delete(p);
    				
    			}
    			
    			personsToDelete = personService.findByCriteria(personsToDeleteQuery);
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
