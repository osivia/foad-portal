package fr.gouv.education.foad.selector.scope.portlet.model;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Scope selector portlet settings.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ScopeSelectorSettings {

    /** Selector identifier. */
    private String selectorId;


    /**
     * Constructor.
     */
    public ScopeSelectorSettings() {
        super();
    }


    /**
     * Getter for selectorId.
     * 
     * @return the selectorId
     */
    public String getSelectorId() {
        return selectorId;
    }

    /**
     * Setter for selectorId.
     * 
     * @param selectorId the selectorId to set
     */
    public void setSelectorId(String selectorId) {
        this.selectorId = selectorId;
    }

}
