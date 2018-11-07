package fr.gouv.education.foad.portlet.repository;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;

/**
 * Taskbar portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see TaskbarRepository
 */
@Repository
public class TaskbarRepositoryImpl implements TaskbarRepository {

    /** Taskbar service. */
    @Autowired
    private ITaskbarService taskbarService;


    /**
     * Constructor.
     */
    public TaskbarRepositoryImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<TaskbarTask> getNavigationTasks(PortalControllerContext portalControllerContext) throws PortletException {
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);
        // Base path
        String basePath = nuxeoController.getBasePath();

        // Navigation tasks
        List<TaskbarTask> navigationTasks;
        if (StringUtils.isEmpty(basePath)) {
            navigationTasks = new ArrayList<TaskbarTask>(0);
        } else {
            try {
                navigationTasks = this.taskbarService.getTasks(portalControllerContext, basePath, true);
            } catch (PortalException e) {
                throw new PortletException(e);
            }
        }

        return navigationTasks;
    }

}
