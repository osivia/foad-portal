package fr.gouv.education.foad.plugin.service;

import java.util.Map;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.CustomizationContext;

import fr.toutatice.portail.cms.nuxeo.api.domain.ListTemplate;

/**
 * Customized member management plugin service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface CustomizedMemberManagementPluginService {

    /**
     * Customize list templates.
     * 
     * @param customizationContext customization context
     * @param listTemplates list templates
     */
    void customizeListTemplates(CustomizationContext customizationContext, Map<String, ListTemplate> listTemplates);


    /**
     * Get workspace search results filter.
     * 
     * @param portalControllerContext portal controller context
     * @return filter
     */
    String getFilter(PortalControllerContext portalControllerContext);

}
