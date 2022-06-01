package fr.gouv.education.foad.customizer.regions;

import java.util.Arrays;
import java.util.Map;

import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.customization.CustomizationModuleMetadatas;
import org.osivia.portal.api.customization.ICustomizationModule;
import org.osivia.portal.api.customization.ICustomizationModulesRepository;
import org.osivia.portal.api.theming.IRenderedRegions;

/**
 * Regions customizer.
 *
 * @author Cédric Krommenhoek
 * @see GenericPortlet
 * @see ICustomizationModule
 */
public class RegionsCustomizer extends GenericPortlet implements ICustomizationModule {

    /** Customizer name. */
    private static final String CUSTOMIZER_NAME = "foad.customizer.regions";
    /** Customization modules repository attribute name. */
    private static final String ATTRIBUTE_CUSTOMIZATION_MODULES_REPOSITORY = "CustomizationModulesRepository";

    /** Charte context path. */
    private static final String CHARTE_CONTEXT_PATH = "/foad-charte";


    /** Customization module metadatas. */
    private final CustomizationModuleMetadatas metadatas;


    /** Customization modules repository. */
    private ICustomizationModulesRepository repository;


    /**
     * Constructor.
     */
    public RegionsCustomizer() {
        super();
        this.metadatas = this.generateMetadatas();
    }


    /**
     * Generate customization module metadatas.
     *
     * @return metadatas
     */
    private CustomizationModuleMetadatas generateMetadatas() {
        CustomizationModuleMetadatas metadatas = new CustomizationModuleMetadatas();
        metadatas.setName(CUSTOMIZER_NAME);
        metadatas.setModule(this);
        metadatas.setCustomizationIDs(Arrays.asList(IRenderedRegions.CUSTOMIZER_ID));
        return metadatas;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void init() throws PortletException {
        super.init();
        this.repository = (ICustomizationModulesRepository) this.getPortletContext().getAttribute(ATTRIBUTE_CUSTOMIZATION_MODULES_REPOSITORY);
        this.repository.register(this.metadatas);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {
        super.destroy();
        this.repository.unregister(this.metadatas);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customize(String customizationID, CustomizationContext context) {
        Map<String, Object> attributes = context.getAttributes();
        IRenderedRegions renderedRegion = (IRenderedRegions) attributes.get(IRenderedRegions.CUSTOMIZER_ATTRIBUTE_RENDERED_REGIONS);

        // Context path
        String contextPath = (String) attributes.get(IRenderedRegions.CUSTOMIZER_ATTRIBUTE_THEME_CONTEXT_PATH);
        
        if (CHARTE_CONTEXT_PATH.equals(contextPath)) {
            // Remove default toolbar region
            renderedRegion.removeRenderedRegion("toolbar");
            // Remove default search region
            renderedRegion.removeRenderedRegion("search");
            // Remove default tabs region
            renderedRegion.removeRenderedRegion("tabs");

            // Replace default breadcrumb region
            renderedRegion.customizeRenderedRegion("breadcrumb", "/regions/breadcrumb.jsp");
            // Replace default drawer toolbar region
            renderedRegion.customizeRenderedRegion("drawer-toolbar", "/regions/drawer-toolbar.jsp");
            // Replace default footer region
            renderedRegion.customizeRenderedRegion("footer", "/regions/footer.jsp");

            // Add toolbar administration region
            renderedRegion.customizeRenderedRegion("toolbar-administration", "/regions/toolbar-administration.jsp");
            // Add toolbar tasks region
            renderedRegion.customizeRenderedRegion("toolbar-tasks", "/regions/toolbar-tasks.jsp");
            // Add toolbar user menu region
            renderedRegion.customizeRenderedRegion("toolbar-user-menu", "/regions/toolbar-user-menu.jsp");
            
            // Add toolbar V2 access
            renderedRegion.customizeRenderedRegion("toolbar-v2-access", "/regions/toolbar-v2-access.jsp");
            
            // Add content title region
            renderedRegion.customizeRenderedRegion("content-title", "/regions/content-title.jsp");
            // Add header title region
            renderedRegion.customizeRenderedRegion("header-title", "/regions/header-title.jsp");
            // Add header logo large region
            renderedRegion.customizeRenderedRegion("header-logo-large", "/regions/header-logo-large.jsp");
            // Add statistics region
            renderedRegion.customizeRenderedRegion("stats", "/regions/stats.jsp");
            // Add workspace tabs region
            renderedRegion.customizeRenderedRegion("workspace-tabs", "/regions/workspace-tabs.jsp");
        }
    }

}
