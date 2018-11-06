package fr.gouv.education.foad.customizer.attributes;

/**
 * Workpace tab java-bean.
 * 
 * @author Cédric Krommenhoek
 */
public class WorkpaceTab {

    /** Title. */
    private String title;
    /** URL. */
    private String url;
    /** Active tab indicator. */
    private boolean active;
    /** Home tab indicator. */
    private boolean home;


    /**
     * Constructor.
     */
    public WorkpaceTab() {
        super();
    }


    /**
     * Getter for title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title.
     * 
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
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

    /**
     * Getter for home.
     * 
     * @return the home
     */
    public boolean isHome() {
        return home;
    }

    /**
     * Setter for home.
     * 
     * @param home the home to set
     */
    public void setHome(boolean home) {
        this.home = home;
    }

}
