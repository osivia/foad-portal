package fr.gouv.education.foad.filebrowser.plugin.model;

import java.util.HashMap;
import java.util.Map;

import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.player.Player;
import org.osivia.portal.core.constants.InternalConstants;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * File browser player.
 * 
 * @author Cédric Krommenhoek
 * @see Player
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileBrowserPlayer extends Player {

    /** File browser portlet instance. */
    private static final String PORTLET_INSTANCE = "foad-file-browser-instance";


    /** Window properties. */
    private final Map<String, String> windowProperties;


    /**
     * Constructor.
     * 
     * @param document document
     */
    public FileBrowserPlayer(Document document) {
        super();

        // Window properties
        this.windowProperties = this.getWindowProperties(document);
    }


    /**
     * Get window properties.
     * 
     * @param document document
     * @return window properties
     */
    private Map<String, String> getWindowProperties(Document document) {
        Map<String, String> properties = new HashMap<>();

        // Decorators
        properties.put("osivia.hideDecorators", "1");

        // AJAX
        properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
        properties.put("osivia.ajaxLink", "1");

        if (document != null) {
            // Path
            properties.put(Constants.WINDOW_PROP_URI, document.getPath());
            // Title
            properties.put(InternalConstants.PROP_WINDOW_TITLE, document.getTitle());
        }

        return properties;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getPortletInstance() {
        return PORTLET_INSTANCE;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, String> getWindowProperties() {
        return this.windowProperties;
    }

}
