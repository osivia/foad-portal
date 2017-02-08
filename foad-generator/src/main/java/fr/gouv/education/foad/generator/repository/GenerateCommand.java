package fr.gouv.education.foad.generator.repository;

import io.codearte.jfairy.Fairy;

import java.io.File;
import java.net.URL;
import java.util.Random;

import org.apache.log4j.Logger;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.FileBlob;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;

import fr.gouv.education.foad.generator.model.Configuration;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Generate command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
public class GenerateCommand implements INuxeoCommand {

	private static Logger LOGGER = Logger.getLogger(GenerateCommand.class);

    /** Generator configuration. */
    private final Configuration configuration;

    private String path;
    private String space_prefix;
    private Fairy fairy;

	private URL realPath;
    
    
    /**
     * Constructor.
     *
     * @param ldapContext LDAP context
     * @param configuration generator configuration
     * @param exampleFile 
     * @param academy academy
     * @param locale locale
     */
    public GenerateCommand(Configuration configuration, String path, String space_prefix, Fairy fairy, URL exampleFile) {
        super();
        this.configuration = configuration;
        this.path = path;
        this.space_prefix = space_prefix;
        this.fairy = fairy;
        this.realPath = exampleFile;
        

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {

        DocumentService documentService = nuxeoSession.getAdapter(DocumentService.class);
        
        Document docRoot = documentService.getDocument(new PathRef(path));
        
        Random random = new Random();
        for(int i = 0; i < configuration.getNbOfRootFolers(); i++) {
        	
			Document folder = createFolder(documentService, docRoot, random, Integer.toString(i));
			
			for(int j = 0; j < configuration.getNbOfSubFolers(); j++) {
				
				Document subfolder = createFolder(documentService, folder, random, Integer.toString(i) + "-" + Integer.toString(j));

				for(int k = 0; k < configuration.getNbOfSubItems(); k++) {
					
					createFile(documentService, random, subfolder, Integer.toString(i) + "-" + Integer.toString(j) + "-" + Integer.toString(k));
				}
				
			}
			
			for(int k = 0; k < configuration.getNbOfSubItems(); k++) {
				
				createFile(documentService, random, folder, Integer.toString(i) + "-" + Integer.toString(k));
				

			}
			
			
        }
        

        return null;
    }


	private void createFile(DocumentService documentService, Random random,
			Document folder, String id) throws Exception {
		PropertyMap properties;
		
		properties = new PropertyMap();
		properties.set("dc:title", fairy.textProducer().latinWord(4));
		String user = "utilisateur-" + space_prefix + "-" + random.nextInt(configuration.getNbOfUsersPerWks()) + "@example.org";
		properties.set("dc:creator", user);
		
		if((random.nextInt(2) % 2) == 0) {
			LOGGER.warn("Create note fichier-tmc-" +space_prefix+ "-" + id);

			properties.set("note:note", fairy.textProducer().latinWord(12));

			documentService.createDocument(folder, "Note", "fichier-tmc-" +space_prefix+ "-" + id, properties);
			
		}
		else {
			LOGGER.warn("Create file fichier-tmc-" +space_prefix+ "-" + id);

			Document file = documentService.createDocument(folder, "File", "fichier-tmc-" +space_prefix+ "-" +id, properties);
			
			//String filepath = Thread.currentThread().getContextClassLoader().getResource(realPath).getPath();
			Blob attachmentBlob = new FileBlob(new File(realPath.getFile()));
			documentService.setBlob(file, attachmentBlob, "file:content");
			
		}
	}


	private Document createFolder(DocumentService documentService,
			Document docRoot, Random random, String id) throws Exception {
		
		LOGGER.warn("Create folder dossier-tmc-" +space_prefix+ "-" +id);
		PropertyMap properties = new PropertyMap();
		properties.set("dc:title", fairy.textProducer().latinWord(4));
		String user = "utilisateur-" + space_prefix + "-" + random.nextInt(configuration.getNbOfUsersPerWks()) + "@example.org";
		properties.set("dc:creator", user);
		Document folder = documentService.createDocument(docRoot, "Folder", "dossier-tmc-" +space_prefix+ "-" +id, properties);
		return folder;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return "";
    }


}
