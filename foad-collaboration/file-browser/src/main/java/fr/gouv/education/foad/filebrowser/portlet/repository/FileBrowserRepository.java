package fr.gouv.education.foad.filebrowser.portlet.repository;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletException;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.cms.impl.BasicPermissions;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.core.cms.CMSBinaryContent;

/**
 * File browser portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface FileBrowserRepository {

    /**
     * Get current path.
     * 
     * @param portalControllerContext portal controller context
     * @return path
     * @throws PortletException
     */
    String getCurrentPath(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get base path.
     * 
     * @param portalControllerContext portal controller context
     * @return path
     * @throws PortletException
     */
    String getBasePath(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get documents.
     * 
     * @param portalControllerContext portal controller context
     * @return documents
     * @throws PortletException
     */
    List<Document> getDocuments(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get permissions.
     * 
     * @param portalControllerContext portal controller context
     * @param document Nuxeo document
     * @return permissions
     * @throws PortletException
     */
    BasicPermissions getPermissions(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Get download URL.
     * 
     * @param portalControllerContext portal controller context
     * @param document Nuxeo document
     * @return URL
     * @throws PortletException
     */
    String getDownloadUrl(PortalControllerContext portalControllerContext, Document document) throws PortletException;


    /**
     * Duplicate document.
     * 
     * @param portalControllerContext portal controller context
     * @param path document path
     * @throws PortletException
     */
    void duplicate(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Delete documents.
     * 
     * @param portalControllerContext portal controller context
     * @param identifiers document identifiers
     * @throws PortletException
     */
    void delete(PortalControllerContext portalControllerContext, List<String> identifiers) throws PortletException;


    /**
     * Get binary content.
     * 
     * @param portalControllerContext portal controller context
     * @param paths document paths
     * @return binary content
     * @throws PortletException
     * @throws IOException
     */
    CMSBinaryContent getBinaryContent(PortalControllerContext portalControllerContext, List<String> paths) throws PortletException, IOException;


    /**
     * Update menubar.
     * 
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void updateMenubar(PortalControllerContext portalControllerContext) throws PortletException;

}
