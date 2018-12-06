package fr.gouv.education.foad.filebrowser.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.gouv.education.foad.filebrowser.portlet.repository.command.GetFileBrowserDocumentsCommand;
import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;

/**
 * File browser portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see FileBrowserRepository
 */
@Repository
public class FileBrowserRepositoryImpl implements FileBrowserRepository {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;


    /**
     * Constructor.
     */
    public FileBrowserRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Document> getDocuments(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Current path
        String path = this.getCurrentPath(portalControllerContext);

        // Document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
        // Document
        Document document = documentContext.getDoc();
        
        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(GetFileBrowserDocumentsCommand.class, document.getId());
        Object result = nuxeoController.executeNuxeoCommand(command);

        // Documents
        List<Document> documents;
        if ((result != null) && (result instanceof Documents)) {
            Documents resultDocuments = (Documents) result;
            documents = resultDocuments.list();
        } else {
            documents = null;
        }

        return documents;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMenubar(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Current path
        String path = this.getCurrentPath(portalControllerContext);

        // Document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
        // Document
        Document document = documentContext.getDoc();

        // Update menubar
        nuxeoController.setCurrentDoc(document);
        nuxeoController.insertContentMenuBarItems();
    }


    /**
     * Get current path.
     * 
     * @param portalControllerContext portal controller context
     * @return path
     * @throws PortletException
     */
    private String getCurrentPath(PortalControllerContext portalControllerContext) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        return window.getProperty(Constants.WINDOW_PROP_URI);
    }

}
