package fr.gouv.education.foad.customizer.plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.taskbar.ITaskbarService;
import org.osivia.portal.api.taskbar.TaskbarFactory;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItems;

import fr.gouv.education.foad.customizer.plugin.fragment.DenyFromLocalAccountsFragment;
import fr.gouv.education.foad.customizer.plugin.fragment.TileFragmentModule;
import fr.gouv.education.foad.customizer.plugin.list.WorkspacesListTemplateModule;
import fr.gouv.education.foad.customizer.plugin.menubar.FoadMenubarModule;
import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;
import fr.toutatice.portail.cms.nuxeo.api.domain.FragmentType;
import fr.toutatice.portail.cms.nuxeo.api.domain.ListTemplate;
import fr.toutatice.portail.cms.nuxeo.api.fragment.FragmentModule;

/**
 * FOAD plugin.
 *
 * @author Cédric Krommenhoek
 * @see AbstractPluginPortlet
 */
public class FoadPlugin extends AbstractPluginPortlet {

    /** Plugin name. */
    private static final String PLUGIN_NAME = "foad.plugin";


    /** Internationalization bundle factory. */
    private final IBundleFactory bundleFactory;


    /**
     * Constructor.
     */
    public FoadPlugin() {
        super();

        // Internationalization bundle factory
        IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
                IInternationalizationService.MBEAN_NAME);
        this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
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
        // Customize fragments
        this.customizeFragments(context);
        // Customize list templates
        this.customizeListTemplates(context);
        // Customize taskbar items
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
     * Customize fragments.
     * 
     * @param context customization context
     */
    private void customizeFragments(CustomizationContext context) {
     // Portlet context
        PortletContext portletContext = this.getPortletContext();
     // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(context.getLocale());
        
        // Fragments types
    	List<FragmentType> fragmentTypes = this.getFragmentTypes(context);
    	
    	// Deny from local accounts
    	FragmentType denyFromLocalAccounts = new FragmentType("denyFromLocalAccounts", "Fragment interdit aux utilisateurs locaux", new DenyFromLocalAccountsFragment(null, true));
    	fragmentTypes.add(denyFromLocalAccounts);
    	
    	// Tile
    	FragmentModule tileModule = new TileFragmentModule(portletContext);
        FragmentType tile = new FragmentType(TileFragmentModule.ID, bundle.getString("FRAGMENT_TILE"), tileModule);
        fragmentTypes.add(tile);
    }


    /**
     * Customize list templates.
     * 
     * @param context customization context
     */
    private void customizeListTemplates(CustomizationContext context) {
        // Portlet context
        PortletContext portletContext = this.getPortletContext();
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(context.getLocale());

        // List templates
        Map<String, ListTemplate> templates = this.getListTemplates(context);


        // Events
        ListTemplate events = new ListTemplate("events", bundle.getString("LIST_TEMPLATE_EVENTS"), "dublincore, toutatice, vevent");
        templates.put(events.getKey(), events);

        // Workspaces
        ListTemplate workspaces = new ListTemplate("workspaces", bundle.getString("LIST_TEMPLATE_WORKSPACES"), "dublincore, toutatice, toutatice_space");
        workspaces.setModule(new WorkspacesListTemplateModule(portletContext));
        templates.put(workspaces.getKey(), workspaces);
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


        // Search
        TaskbarItem search = factory.createStapledTaskbarItem(ITaskbarService.SEARCH_TASK_ID, "SEARCH_TASK", null, "/default/templates/workspace/search");
        factory.hide(search, true);
        items.add(search);


        // Last modifications
        TaskbarItem lastModifications = factory.createCmsTaskbarItem("LAST_MODIFICATIONS", "WORKSPACE_LAST_MODIFICATIONS_TASK", null, null);
        lastModifications.setToDefault(1);
        factory.hide(lastModifications, true);
        items.add(lastModifications);


        // Remove items
        for (String id : Arrays.asList("GALLERY")) {
            items.remove(id);
        }
    }

}
