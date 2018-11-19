package fr.gouv.education.foad.portlet.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.portal.api.panels.PanelPlayer;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarItemType;
import org.osivia.portal.api.taskbar.TaskbarTask;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.gouv.education.foad.portlet.model.FolderTask;
import fr.gouv.education.foad.portlet.model.ServiceTask;
import fr.gouv.education.foad.portlet.model.Task;
import fr.gouv.education.foad.portlet.model.Taskbar;
import fr.gouv.education.foad.portlet.repository.TaskbarRepository;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


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
                    } else if (TaskbarItemType.CMS.equals(navigationTask.getType()) && "Room".equals(navigationTask.getDocumentType())) {
                        // Ignore rooms
                    } else {
                        services.add(navigationTask);
                    }
                }
            }
        }


        // Taskbar
        Taskbar taskbar = this.applicationContext.getBean(Taskbar.class);
        taskbar.setFolders(this.createTasks(portalControllerContext, bundle, activeId, folders, FolderTask.class));
        taskbar.setServices(this.createTasks(portalControllerContext, bundle, activeId, services, ServiceTask.class));

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
     * @param clazz tasks class
     * @return tasks
     * @throws PortletException
     */
    private <T extends Task> List<T> createTasks(PortalControllerContext portalControllerContext, Bundle bundle, String activeId,
            List<TaskbarTask> navigationTasks, Class<T> clazz) throws PortletException {
        List<T> tasks;

        if (CollectionUtils.isEmpty(navigationTasks)) {
            tasks = null;
        } else {
            tasks = new ArrayList<>(navigationTasks.size());

            for (TaskbarTask navigationTask : navigationTasks) {
                T task = this.createTask(portalControllerContext, bundle, activeId, navigationTask, clazz);
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
     * @param clazz task class
     * @return task
     * @throws PortletException
     */
    private <T extends Task> T createTask(PortalControllerContext portalControllerContext, Bundle bundle, String activeId, TaskbarTask navigationTask,
            Class<T> clazz) throws PortletException {
        T task = this.applicationContext.getBean(clazz);

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


        if (active && (task instanceof FolderTask)) {
            // Folder
            FolderTask folder = (FolderTask) task;

            // Folder path
            folder.setPath(navigationTask.getPath());

            // Folder navigation tree
            this.repository.generateFolderNavigationTree(portalControllerContext, folder);
        }


        if (task instanceof ServiceTask) {
            // Service
            ServiceTask service = (ServiceTask) task;

            // Icon
            String icon = navigationTask.getIcon();
            service.setIcon(icon);

            // Type, useful for coloration
            String type = StringUtils.lowerCase(navigationTask.getId());
            service.setType(type);
        }

        return task;
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public void drop(PortalControllerContext portalControllerContext, List<String> sourceIds, String targetId) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();

        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(request.getLocale());

        try {
            this.repository.moveDocuments(portalControllerContext, sourceIds, targetId);

            // Refresh navigation
            request.setAttribute(Constants.PORTLET_ATTR_UPDATE_CONTENTS, Constants.PORTLET_VALUE_ACTIVATE);

            // Update public render parameter for associated portlets refresh
            response.setRenderParameter("dnd-update", String.valueOf(System.currentTimeMillis()));

            // Notification
            String message;
            if (sourceIds.size() == 1) {
                message = bundle.getString("DOCUMENT_MOVE_SUCCESS_MESSAGE");
            } else {
                message = bundle.getString("DOCUMENTS_MOVE_SUCCESS_MESSAGE", sourceIds.size());
            }
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.SUCCESS);
        } catch (NuxeoException e) {
            // Notification
            String message;
            if (sourceIds.size() == 1) {
                message = bundle.getString("DOCUMENT_MOVE_WARNING_MESSAGE");
            } else {
                message = bundle.getString("DOCUMENTS_MOVE_WARNING_MESSAGE", sourceIds.size());
            }
            this.notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.WARNING);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray getFolderChildren(PortalControllerContext portalControllerContext, String path) throws PortletException {
        // Children
        SortedSet<FolderTask> children = this.repository.getFolderChildren(portalControllerContext, path);

        // JSON array
        JSONArray array = new JSONArray();
        if (CollectionUtils.isNotEmpty(children)) {
            for (FolderTask child : children) {
                // JSON object
                JSONObject object = new JSONObject();

                // Display name
                object.put("title", child.getDisplayName());
                // URL
                object.put("href", child.getUrl());
                // Folder indicator
                object.put("folder", child.isFolder());
                // Lazy indicator
                object.put("lazy", child.isLazy());
                // Path
                object.put("path", child.getPath());

                array.add(object);
            }

        }

        return array;
    }

}
