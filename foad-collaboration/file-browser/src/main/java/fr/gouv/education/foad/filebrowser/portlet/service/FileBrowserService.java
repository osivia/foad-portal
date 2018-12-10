package fr.gouv.education.foad.filebrowser.portlet.service;

import java.io.IOException;
import java.util.List;

import javax.portlet.PortletException;

import org.dom4j.Element;
import org.osivia.portal.api.context.PortalControllerContext;

import fr.gouv.education.foad.filebrowser.portlet.model.FileBrowserBulkDownloadContent;
import fr.gouv.education.foad.filebrowser.portlet.model.FileBrowserForm;
import fr.gouv.education.foad.filebrowser.portlet.model.FileBrowserSort;

/**
 * File browser portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface FileBrowserService {

    /**
     * Get form.
     * 
     * @param portalControllerContext portal controller context
     * @return form
     * @throws PortletException
     */
    FileBrowserForm getForm(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Sort file browser items.
     * 
     * @param portalControllerContext portal controller context
     * @param form form
     * @param sort sort
     * @param alt alternative sort indicator
     * @throws PortletException
     */
    void sortItems(PortalControllerContext portalControllerContext, FileBrowserForm form, FileBrowserSort sort, boolean alt) throws PortletException;


    /**
     * Get file browser toolbar DOM element.
     * 
     * @param portalControllerContext portal controller context
     * @param indexes selected items indexes
     * @return DOM element
     * @throws PortletException
     */
    Element getToolbar(PortalControllerContext portalControllerContext, List<String> indexes) throws PortletException;


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
     * @throws IOException
     */
    void delete(PortalControllerContext portalControllerContext, List<String> identifiers) throws PortletException, IOException;


    /**
     * Get bulk download content.
     * 
     * @param portalControllerContext portal controller context
     * @param paths document paths
     * @return content
     * @throws PortletException
     * @throws IOException
     */
    FileBrowserBulkDownloadContent getBulkDownload(PortalControllerContext portalControllerContext, List<String> paths) throws PortletException, IOException;


    /**
     * Update menubar.
     * 
     * @param portalControllerContext portal controller context
     * @throws PortletException
     */
    void updateMenubar(PortalControllerContext portalControllerContext) throws PortletException;

}
