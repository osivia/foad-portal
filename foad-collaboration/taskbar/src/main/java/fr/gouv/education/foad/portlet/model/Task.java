package fr.gouv.education.foad.portlet.model;

/**
 * Task java-bean abstract super-class.
 * 
 * @author Cédric Krommenhoek
 */
public abstract class Task {

    /** Display name. */
    private String displayName;
    /** URL. */
    private String url;
    /** Active indicator. */
    private boolean active;


    /**
     * Constructor.
     */
    public Task() {
        super();
    }


    /**
     * Getter for displayName.
     * 
     * @return the displayName
     */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Setter for displayName.
     * 
     * @param displayName the displayName to set
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * Getter for url.
     * 
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter for url.
     * 
     * @param url the url to set
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter for active.
     * 
     * @return the active
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Setter for active.
     * 
     * @param active the active to set
     */
    public void setActive(boolean active) {
        this.active = active;
    }

}
