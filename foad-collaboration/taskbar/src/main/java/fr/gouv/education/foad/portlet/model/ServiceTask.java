package fr.gouv.education.foad.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Service java-bean.
 * 
 * @author Cédric Krommenhoek
 * @see Task
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ServiceTask extends Task {

    /** Icon. */
    private String icon;
    /** Type, useful for coloration. */
    private String type;


    /**
     * Constructor.
     */
    public ServiceTask() {
        super();
    }


    /**
     * Getter for type.
     * 
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Setter for type.
     * 
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Getter for icon.
     * 
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Setter for icon.
     * 
     * @param icon the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

}
