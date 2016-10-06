package fr.gouv.education.foad.customizer.plugin;

import java.util.List;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarModule;

import fr.gouv.education.foad.customizer.plugin.menubar.FoadMenubarModule;
import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

/**
 * FOAD plugin.
 *
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
public class FoadPlugin extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "foad.plugin";


    /**
     * Constructor.
     */
    public FoadPlugin() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected String getPluginName() {
        return PLUGIN_NAME;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(String customizationID, CustomizationContext context) {
        // Customize menubar modules
        this.customizeMenubarModules(context);
    }


    /**
     * Customize menubar modules.
     * 
     * @param context customization context
     */
    private void customizeMenubarModules(CustomizationContext context) {
        // Menubar modules
        List<MenubarModule> modules = this.getMenubarModules(context);

        // FOAD menubar module
        MenubarModule foadModule = new FoadMenubarModule();
        modules.add(foadModule);
    }

}
