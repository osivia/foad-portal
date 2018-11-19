package fr.gouv.education.foad.portlet.repository;

import java.util.List;
import java.util.SortedSet;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.taskbar.TaskbarTask;

import fr.gouv.education.foad.portlet.model.FolderTask;
import fr.gouv.education.foad.portlet.model.ServiceTask;

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


    List<ServiceTask> getAdministration(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Generate folder tree.
     * 
     * @param portalControllerContext portal controller context
     * @param folder folder
     * @throws PortletException
     */
    void generateFolderNavigationTree(PortalControllerContext portalControllerContext, FolderTask folder) throws PortletException;


    /**
     * Get folder children.
     * 
     * @param portalControllerContext portal controller context
     * @param path folder path
     * @return folder children
     * @throws PortletException
     */
    SortedSet<FolderTask> getFolderChildren(PortalControllerContext portalControllerContext, String path) throws PortletException;


    /**
     * Move documents.
     * 
     * @param portalControllerContext portal controller context
     * @param sourceIds source identifiers
     * @param targetId target identifier
     * @throws PortletException
     */
    void moveDocuments(PortalControllerContext portalControllerContext, List<String> sourceIds, String targetId) throws PortletException;

}
