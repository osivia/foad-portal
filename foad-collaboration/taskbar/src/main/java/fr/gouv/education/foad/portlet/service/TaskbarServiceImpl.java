package fr.gouv.education.foad.portlet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarItemType;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.gouv.education.foad.portlet.model.Task;
import fr.gouv.education.foad.portlet.model.Taskbar;
import fr.gouv.education.foad.portlet.repository.TaskbarRepository;

/**
 * Taskbar portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see TaskbarService
 */
@Service
public class TaskbarServiceImpl implements TaskbarService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private TaskbarRepository repository;

    /** Portal URL factory. */
    @Autowired
    private IPortalUrlFactory portalUrlFactory;

    /** Taskbar service. */
    @Autowired
    private ITaskbarService taskbarService;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public TaskbarServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Taskbar getTaskbar(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());


        // Navigation tasks
        List<TaskbarTask> navigationTasks = this.repository.getNavigationTasks(portalControllerContext);

        // Active navigation task identifier
        String activeId;
        try {
            activeId = this.taskbarService.getActiveId(portalControllerContext);
        } catch (PortalException e) {
            throw new PortletException(e);
        }

        // Separate folders and services
        List<TaskbarTask> folders;
        List<TaskbarTask> services;
        if (CollectionUtils.isEmpty(navigationTasks)) {
            folders = null;
            services = null;
        } else {
            folders = new ArrayList<>();
            services = new ArrayList<>();

            for (TaskbarTask navigationTask : navigationTasks) {
                if (!navigationTask.isDisabled() && !navigationTask.isHidden()) {
                    if (TaskbarItemType.CMS.equals(navigationTask.getType()) && "Folder".equals(navigationTask.getDocumentType())) {
                        folders.add(navigationTask);
                    } else {
                        services.add(navigationTask);
                    }
                }
            }
        }


        // Taskbar
        Taskbar taskbar = this.applicationContext.getBean(Taskbar.class);
        taskbar.setFolders(this.createTasks(portalControllerContext, bundle, activeId, folders));
        taskbar.setServices(this.createTasks(portalControllerContext, bundle, activeId, services));

        // Administration
        // TODO

        return taskbar;
    }


    /**
     * Create tasks from navigation tasks.
     * 
     * @param portalControllerContext portal controller context
     * @param bundle internationalization bundle
     * @param activeId active navigation task identifier
     * @param navigationTasks navigation tasks
     * @return tasks
     * @throws PortletException
     */
    private List<Task> createTasks(PortalControllerContext portalControllerContext, Bundle bundle, String activeId, List<TaskbarTask> navigationTasks)
            throws PortletException {
        List<Task> tasks;

        if (CollectionUtils.isEmpty(navigationTasks)) {
            tasks = null;
        } else {
            tasks = new ArrayList<>(navigationTasks.size());

            for (TaskbarTask navigationTask : navigationTasks) {
                Task task = this.createTask(portalControllerContext, bundle, activeId, navigationTask);
                tasks.add(task);
            }
        }

        return tasks;
    }


    /**
     * Create task from navigation task.
     * 
     * @param portalControllerContext portal controller context
     * @param bundle internationalization bundle
     * @param activeId active navigation task identifier
     * @param navigationTask navigation task
     * @return task
     * @throws PortletException
     */
    private Task createTask(PortalControllerContext portalControllerContext, Bundle bundle, String activeId, TaskbarTask navigationTask)
            throws PortletException {
        Task task = this.applicationContext.getBean(Task.class);

        // Display name
        String displayName;
        if (StringUtils.isEmpty(navigationTask.getTitle())) {
            displayName = bundle.getString(navigationTask.getKey(), navigationTask.getCustomizedClassLoader());
        } else {
            displayName = navigationTask.getTitle();
        }
        task.setDisplayName(displayName);

        // URL
        String url;
        if (navigationTask.getPlayer() != null) {
            // Start portlet URL
            PanelPlayer player = navigationTask.getPlayer();

            // Window properties
            Map<String, String> properties = new HashMap<String, String>();
            if (player.getProperties() != null) {
                properties.putAll(player.getProperties());
            }
            properties.put(ITaskbarService.TASK_ID_WINDOW_PROPERTY, navigationTask.getId());
            if (StringUtils.isNotEmpty(displayName)) {
                properties.put("osivia.title", displayName);
            }
            properties.put("osivia.back.reset", String.valueOf(true));
            properties.put("osivia.navigation.reset", String.valueOf(true));

            try {
                url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext, player.getInstance(), properties);
            } catch (PortalException e) {
                throw new PortletException(e);
            }
        } else if (StringUtils.isNotEmpty(navigationTask.getPath())) {
            // CMS URL
            String path = navigationTask.getPath();

            url = this.portalUrlFactory.getCMSUrl(portalControllerContext, null, path, null, null, "taskbar", null, null, "1", null);
        } else {
            // Unknown case
            url = "#";
        }
        task.setUrl(url);

        // Active indicator
        boolean active = StringUtils.equals(activeId, navigationTask.getId());
        task.setActive(active);

        // Icon
        String icon = navigationTask.getIcon();
        task.setIcon(icon);

        // Type, useful for coloration
        String type = StringUtils.lowerCase(navigationTask.getId());
        task.setType(type);

        return task;
    }

}
