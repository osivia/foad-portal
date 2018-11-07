package fr.gouv.education.foad.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

import fr.gouv.education.foad.portlet.model.Taskbar;

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

}
