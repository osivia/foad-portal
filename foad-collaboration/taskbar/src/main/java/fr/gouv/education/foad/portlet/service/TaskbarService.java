package fr.gouv.education.foad.portlet.service;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

import fr.gouv.education.foad.portlet.model.Taskbar;
import net.sf.json.JSONArray;

/**
 * Taskbar portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface TaskbarService {

    /**
     * Get taskbar.
     * 
     * @param portalControllerContext portal controller context
     * @return taskbar
     * @throws PortletException
     */
    Taskbar getTaskbar(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Drop.
     * 
     * @param portalControllerContext portal controller context
     * @param sourceIds source identifiers
     * @param targetId target identifier
     * @throws PortletException
     */
    void drop(PortalControllerContext portalControllerContext, List<String> sourceIds, String targetId) throws PortletException;


    /**
     * Get folder children.
     * 
     * @param portalControllerContext portal controller context
     * @param path parent folder path
     * @return JSON array
     * @throws PortletException
     */
    JSONArray getFolderChildren(PortalControllerContext portalControllerContext, String path) throws PortletException;

}
