package fr.gouv.education.foad.customizer.plugin;

import java.util.List;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.taskbar.TaskbarFactory;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItems;

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
        // Taskbar items
        this.customizeTaskbarItems(context);
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


    /**
     * Customize taskbar items.
     *
     * @param context customization context
     */
    private void customizeTaskbarItems(CustomizationContext context) {
        // Taskbar items
        TaskbarItems items = this.getTaskbarItems(context);
        // Factory
        TaskbarFactory factory = this.getTaskbarService().getFactory();

        // Forum
        TaskbarItem forum = factory.createCmsTaskbarItem("FORUMS", "FORUMS_TASK", "glyphicons glyphicons-conversation", "Folder");
        items.add(forum);
    }

}
