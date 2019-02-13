package fr.gouv.education.foad.plugin.controller;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;

import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.services.workspace.plugin.MemberManagementPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import fr.gouv.education.foad.plugin.service.CustomizedMemberManagementPluginService;
import fr.toutatice.portail.cms.nuxeo.api.domain.ListTemplate;

@Controller
public class CustomizedMemberManagementPluginController extends MemberManagementPlugin {

    /** Portlet config. */
    @Autowired
    private PortletConfig portletConfig;

    /** Plugin service. */
    @Autowired
    private CustomizedMemberManagementPluginService service;


    /**
     * Constructor.
     */
    public CustomizedMemberManagementPluginController() {
        super();
    }


    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }


    /**
     * Pre-destroy.
     */
    @PreDestroy
    public void preDestroy() {
        super.destroy();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void customizeCMSProperties(String customizationId, CustomizationContext customizationContext) {
        super.customizeCMSProperties(customizationId, customizationContext);

        // Customize list templates
        Map<String, ListTemplate> listTemplates = this.getListTemplates(customizationContext);
        this.service.customizeListTemplates(customizationContext, listTemplates);
    }

}
