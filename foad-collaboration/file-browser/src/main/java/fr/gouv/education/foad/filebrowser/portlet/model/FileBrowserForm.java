package fr.gouv.education.foad.filebrowser.portlet.model;

import java.util.List;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * File browser form java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileBrowserForm {

    /** File browser items. */
    private List<FileBrowserItem> items;


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

}
