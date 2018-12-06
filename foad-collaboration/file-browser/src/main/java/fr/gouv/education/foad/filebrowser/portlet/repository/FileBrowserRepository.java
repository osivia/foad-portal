package fr.gouv.education.foad.filebrowser.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;

/**
 * File browser portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface FileBrowserRepository {

    /**
     * Get documents.
     * 
     * @param portalControllerContext portal controller context
     * @return documents
     * @throws PortletException
     */
    List<Document> getDocuments(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Update menubar.
     * 
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void updateMenubar(PortalControllerContext portalControllerContext) throws PortletException;

}
