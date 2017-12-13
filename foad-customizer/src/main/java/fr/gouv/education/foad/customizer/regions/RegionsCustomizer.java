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
            // Replace default toolbar region
            renderedRegion.customizeRenderedRegion("toolbar", "/regions/toolbar.jsp");
            // Replace default drawer toolbar region
            renderedRegion.customizeRenderedRegion("drawer-toolbar", "/regions/drawer-toolbar.jsp");
            // Add condensed toolbar region
            renderedRegion.customizeRenderedRegion("condensed-toolbar", "/regions/condensed-toolbar.jsp");
            // Add logo region
            renderedRegion.customizeRenderedRegion("logo", "/regions/logo.jsp");
            // Add statistics region
            renderedRegion.customizeRenderedRegion("stats", "/regions/stats.jsp");
        }
    }

}
