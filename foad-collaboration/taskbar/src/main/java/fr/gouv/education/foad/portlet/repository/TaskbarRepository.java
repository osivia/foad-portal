package fr.gouv.education.foad.portlet.repository;

import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.taskbar.TaskbarTask;

/**
 * Taskbar portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface TaskbarRepository {

    /**
     * Get navigation tasks.
     * 
     * @param portalControllerContext portal controller context
     * @return tasks
     * @throws PortletException
     */
    List<TaskbarTask> getNavigationTasks(PortalControllerContext portalControllerContext) throws PortletException;

}
