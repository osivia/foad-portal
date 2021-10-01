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
        int folderId = 1;
        int fileId = 1;
        
        for(int i = 1; i <= configuration.getNbOfRootFolers(); i++) {
        	
			Document folder = createFolder(documentService, docRoot, random, Integer.toString(folderId));
			folderId = folderId +1;
			
			for(int j = 1; j <= configuration.getNbOfSubFolers(); j++) {
				
				Document subfolder = createFolder(documentService, folder, random, Integer.toString(folderId));
				folderId = folderId +1;

				for(int k = 1; k <= configuration.getNbOfSubItems(); k++) {
					
					createFile(documentService, random, subfolder, Integer.toString(fileId));
					fileId = fileId + 1;
				}
				
			}
			
			for(int k = 1; k <= configuration.getNbOfSubItems(); k++) {
				
				createFile(documentService, random, folder, Integer.toString(fileId));
				fileId = fileId + 1;

			}
			
			
        }
        

        return null;
    }


	private void createFile(DocumentService documentService, Random random,
			Document folder, String id) throws Exception {
		PropertyMap properties;
		
		properties = new PropertyMap();
		properties.set("dc:title", "fichier-tmc-" +space_prefix+ "-" + id);
		String user = "utilisateur-" + space_prefix + "-" + random.nextInt(configuration.getNbOfUsersPerWks()) + "@example.org";
		properties.set("dc:creator", user);
		properties.set("ttc:webid", "fichier-tmc-" +space_prefix+ "-" + id);

		
		if((random.nextInt(2) % 2) == 0) {
			LOGGER.debug("Create note fichier-tmc-" +space_prefix+ "-" + id);
			properties.set("note:note", fairy.textProducer().latinWord(12));

			documentService.createDocument(folder, "Note", "fichier-tmc-" +space_prefix+ "-" + id, properties);
			
		}
		else {
			LOGGER.debug("Create file fichier-tmc-" +space_prefix+ "-" + id);

			Document file = documentService.createDocument(folder, "File", "fichier-tmc-" +space_prefix+ "-" +id, properties);
			
			//String filepath = Thread.currentThread().getContextClassLoader().getResource(realPath).getPath();
			Blob attachmentBlob = new FileBlob(new File(realPath.getFile()));
			documentService.setBlob(file, attachmentBlob, "file:content");
			
		}
	}


	private Document createFolder(DocumentService documentService,
			Document docRoot, Random random, String id) throws Exception {
		
		LOGGER.info("Create folder dossier-tmc-" +space_prefix+ "-" +id);
		PropertyMap properties = new PropertyMap();
		properties.set("dc:title", "dossier-tmc-" +space_prefix+ "-" + id);
		properties.set("ttc:webid", "dossier-tmc-" +space_prefix+ "-" + id);

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
        return "generator" + space_prefix;
    }


}
