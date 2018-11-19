package fr.gouv.education.foad.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Taskbar java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class Taskbar {

    /** Folders. */
    private List<FolderTask> folders;
    /** Services. */
    private List<ServiceTask> services;
    /** Administration. */
    private List<ServiceTask> administration;


    /**
     * Constructor.
     */
    public Taskbar() {
        super();
    }


    /**
     * Getter for folders.
     * 
     * @return the folders
     */
    public List<FolderTask> getFolders() {
        return folders;
    }

    /**
     * Setter for folders.
     * 
     * @param folders the folders to set
     */
    public void setFolders(List<FolderTask> folders) {
        this.folders = folders;
    }

    /**
     * Getter for services.
     * 
     * @return the services
     */
    public List<ServiceTask> getServices() {
        return services;
    }

    /**
     * Setter for services.
     * 
     * @param services the services to set
     */
    public void setServices(List<ServiceTask> services) {
        this.services = services;
    }

    /**
     * Getter for administration.
     * 
     * @return the administration
     */
    public List<ServiceTask> getAdministration() {
        return administration;
    }

    /**
     * Setter for administration.
     * 
     * @param administration the administration to set
     */
    public void setAdministration(List<ServiceTask> administration) {
        this.administration = administration;
    }

}
