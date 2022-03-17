package fr.gouv.education.foad.filebrowser.portlet.repository.command;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.nuxeo.ecm.automation.client.adapters.DocumentService;
import org.nuxeo.ecm.automation.client.model.Blob;
import org.nuxeo.ecm.automation.client.model.Blobs;
import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.PathRef;
import org.nuxeo.ecm.automation.client.model.PropertyMap;
import org.nuxeo.ecm.automation.client.model.StreamBlob;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;

/**
 * Import files Nuxeo command.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ImportFilesCommand implements INuxeoCommand {

    /** Current path. */
    private final String path;
    /** Upload multipart files. */
    private final List<MultipartFile> upload;


    /**
     * Constructor.
     * 
     * @param path current path
     * @param upload upload multipart files
     */
    public ImportFilesCommand(String path, List<MultipartFile> upload) {
        super();
        this.path = path;
        this.upload = upload;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
    	
		DocumentService adapter = nuxeoSession.getAdapter(DocumentService.class);
    	
        // Blobs
        Blobs blobs = new Blobs(this.upload.size());
        for (MultipartFile multipartFile : this.upload) {
        	
        	// special files
        	if (StringUtils.endsWithIgnoreCase(multipartFile.getOriginalFilename(), ".docxf") ||
        			StringUtils.endsWithIgnoreCase(multipartFile.getOriginalFilename(), ".oform")) {
        		
            	PropertyMap properties = new PropertyMap();
            	properties.set("dc:title", multipartFile.getOriginalFilename());
            	
            	String doctype = "DocxfFile";
            	if(StringUtils.endsWithIgnoreCase(multipartFile.getOriginalFilename(), ".oform")) {
            		doctype = "OformFile";
            	}
            	
        		Document doc = adapter.createDocument(new PathRef(path), doctype, null, properties);
            	
        		Blob binary = new StreamBlob(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), 
        				multipartFile.getContentType());
        		
            	adapter.setBlob(doc, binary, "file:content");
        	}
        	else {

                Blob blob = new StreamBlob(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), multipartFile.getContentType());
                blobs.add(blob);	
        	}
        	
        }

        if(blobs.size() > 0) {
	        // Operation request
	        OperationRequest operationRequest = nuxeoSession.newRequest("FileManager.Import");
	        operationRequest.setInput(blobs);
	        operationRequest.setHeader("nx_es_sync", String.valueOf(true));
	        operationRequest.setContextProperty("currentDocument", this.path);
	        operationRequest.set("overwite", true);
	        operationRequest.execute();
        }

        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        return null;
    }

}
