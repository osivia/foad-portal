package fr.gouv.education.foad.filebrowser.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.nuxeo.ecm.automation.client.model.Documents;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cms.impl.BasicPermissions;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.urls.Link;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.osivia.portal.core.cms.CMSException;
import org.osivia.portal.core.cms.CMSServiceCtx;
import org.osivia.portal.core.cms.ICMSService;
import org.osivia.portal.core.cms.ICMSServiceLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import fr.gouv.education.foad.filebrowser.portlet.repository.command.CopyDocumentCommand;
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

    /** CMS service locator. */
    @Autowired
    private ICMSServiceLocator cmsServiceLocator;


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
    public String getCurrentPath(PortalControllerContext portalControllerContext) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        return window.getProperty(Constants.WINDOW_PROP_URI);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getBasePath(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        return nuxeoController.getBasePath();
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
    public BasicPermissions getPermissions(PortalControllerContext portalControllerContext, Document document) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Nuxeo document context
        NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(document.getPath());

        return documentContext.getPermissions(BasicPermissions.class);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getDownloadUrl(PortalControllerContext portalControllerContext, Document document) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Link
        Link link = nuxeoController.getLink(document, "download");

        // URL
        String url;
        if (link == null) {
            url = "#";
        } else {
            url = link.getUrl();
        }

        return url;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void duplicate(PortalControllerContext portalControllerContext, String sourcePath) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Target path
        String targetPath = this.getCurrentPath(portalControllerContext);
        
        // Nuxeo command
        INuxeoCommand command = this.applicationContext.getBean(CopyDocumentCommand.class, sourcePath, targetPath);
        nuxeoController.executeNuxeoCommand(command);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(PortalControllerContext portalControllerContext, List<String> identifiers) throws PortletException {
        // CMS service
        ICMSService cmsService = this.cmsServiceLocator.getCMSService();
        // CMS context
        CMSServiceCtx cmsContext = new CMSServiceCtx();
        cmsContext.setPortalControllerContext(portalControllerContext);

        for (String identifier : identifiers) {
            try {
                cmsService.putDocumentInTrash(cmsContext, identifier);
            } catch (CMSException e) {
                throw new PortletException(e);
            }
        }
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

}
