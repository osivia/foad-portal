package fr.gouv.education.foad.plugin.model;

import javax.portlet.PortletContext;

import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.services.workspace.plugin.portlet.RequestsListTemplateModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.gouv.education.foad.plugin.service.CustomizedMemberManagementPluginService;

/**
 * Workspace search results list template module.
 * 
 * @author Cédric Krommenhoek
 * @see RequestsListTemplateModule
 */
@Component
public class WorkspaceSearchResultsListTemplateModule extends RequestsListTemplateModule {

    /** Portlet context. */
    @Autowired
    private PortletContext portletContext;

    /** Plugin service. */
    @Autowired
    private CustomizedMemberManagementPluginService service;


    /**
     * Constructor.
     */
    public WorkspaceSearchResultsListTemplateModule() {
        super(null);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilter(PortalControllerContext portalControllerContext) {
        return this.service.getFilter(portalControllerContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public PortletContext getPortletContext() {
        return this.portletContext;
    }

}
