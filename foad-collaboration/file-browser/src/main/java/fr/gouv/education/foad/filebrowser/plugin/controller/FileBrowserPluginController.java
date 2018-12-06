package fr.gouv.education.foad.filebrowser.plugin.controller;

import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.PortletException;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.player.IPlayerModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.portlet.context.PortletConfigAware;

import fr.gouv.education.foad.filebrowser.plugin.model.FileBrowserPlayerModule;
import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * File browser plugin controller.
 * 
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
@Controller
public class FileBrowserPluginController extends AbstractPluginPortlet implements PortletConfigAware {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "foad.file-browser.plugin";


    /** File browser player module. */
    @Autowired
    private FileBrowserPlayerModule playerModule;


    /**
     * Constructor.
     */
    public FileBrowserPluginController() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setPortletConfig(PortletConfig portletConfig) {
        try {
            super.init(portletConfig);
        } catch (PortletException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getPluginName() {
        return PLUGIN_NAME;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getOrder() {
        return DEFAULT_DEPLOYMENT_ORDER + 1;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeCMSProperties(String customizationId, CustomizationContext customizationContext) {
        // Players
        this.customizePlayers(customizationContext);
    }


    /**
     * Customize players.
     * 
     * @param customizationContext customization context
     */
    private void customizePlayers(CustomizationContext customizationContext) {
        @SuppressWarnings("rawtypes")
        List<IPlayerModule> players = this.getPlayers(customizationContext);
        
        // File browser player module
        players.add(this.playerModule);
    }

}
