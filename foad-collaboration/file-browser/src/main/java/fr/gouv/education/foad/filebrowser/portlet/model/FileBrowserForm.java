package fr.gouv.education.foad.filebrowser.portlet.model;

import java.util.List;

import org.osivia.portal.api.portlet.Refreshable;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

/**
 * File browser form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(WebApplicationContext.SCOPE_SESSION)
@Refreshable
public class FileBrowserForm {

    /** File browser items. */
    private List<FileBrowserItem> items;
    /** File browser sort criteria. */
    private FileBrowserSortCriteria criteria;
    /** Initialized indicator. */
    private boolean initialized;


    /**
     * Constructor.
     */
    public FileBrowserForm() {
        super();
    }


    /**
     * Getter for items.
     * 
     * @return the items
     */
    public List<FileBrowserItem> getItems() {
        return items;
    }

    /**
     * Setter for items.
     * 
     * @param items the items to set
     */
    public void setItems(List<FileBrowserItem> items) {
        this.items = items;
    }

    /**
     * Getter for criteria.
     * 
     * @return the criteria
     */
    public FileBrowserSortCriteria getCriteria() {
        return criteria;
    }

    /**
     * Setter for criteria.
     * 
     * @param criteria the criteria to set
     */
    public void setCriteria(FileBrowserSortCriteria criteria) {
        this.criteria = criteria;
    }

    /**
     * Getter for initialized.
     * 
     * @return the initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * Setter for initialized.
     * 
     * @param initialized the initialized to set
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

}
