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
    private List<Task> folders;
    /** Services. */
    private List<Task> services;
    /** Administration. */
    private List<Task> administration;


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
    public List<Task> getFolders() {
        return folders;
    }

    /**
     * Setter for folders.
     * 
     * @param folders the folders to set
     */
    public void setFolders(List<Task> folders) {
        this.folders = folders;
    }

    /**
     * Getter for services.
     * 
     * @return the services
     */
    public List<Task> getServices() {
        return services;
    }

    /**
     * Setter for services.
     * 
     * @param services the services to set
     */
    public void setServices(List<Task> services) {
        this.services = services;
    }

    /**
     * Getter for administration.
     * 
     * @return the administration
     */
    public List<Task> getAdministration() {
        return administration;
    }

    /**
     * Setter for administration.
     * 
     * @param administration the administration to set
     */
    public void setAdministration(List<Task> administration) {
        this.administration = administration;
    }

}
