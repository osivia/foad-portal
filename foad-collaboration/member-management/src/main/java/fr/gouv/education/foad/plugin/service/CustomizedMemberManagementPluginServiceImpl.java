package fr.gouv.education.foad.plugin.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Name;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.directory.v2.model.Group;
import org.osivia.portal.api.directory.v2.service.GroupService;
import org.osivia.portal.api.directory.v2.service.PersonService;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.gouv.education.foad.plugin.model.WorkspaceSearchResultsListTemplateModule;
import fr.toutatice.portail.cms.nuxeo.api.domain.ListTemplate;
import fr.toutatice.portail.cms.nuxeo.api.portlet.IPortletModule;
import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Customized member management plugin service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see CustomizedMemberManagementPluginService
 */
@Service
public class CustomizedMemberManagementPluginServiceImpl implements CustomizedMemberManagementPluginService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Internationalization bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Person service. */
    @Autowired
    private PersonService personService;

    /** Group service. */
    @Autowired
    private GroupService groupService;


    /**
     * Constructor.
     */
    public CustomizedMemberManagementPluginServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeListTemplates(CustomizationContext customizationContext, Map<String, ListTemplate> listTemplates) {
        // Internationalization bundle
        Bundle bundle = this.bundleFactory.getBundle(customizationContext.getLocale());
        
        // Workspace search results
        ListTemplate workspaceSearchResults = new ListTemplate("workspace-search-results", bundle.getString("LIST_TEMPLATE_WORKSPACE_SEARCH_RESULTS"), "*");
        IPortletModule workspaceSearchResultsModule = this.applicationContext.getBean(WorkspaceSearchResultsListTemplateModule.class);
        workspaceSearchResults.setModule(workspaceSearchResultsModule);
        listTemplates.put(workspaceSearchResults.getKey(), workspaceSearchResults);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getFilter(PortalControllerContext portalControllerContext) {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Current user
        String user = request.getRemoteUser();

        // User values
        Set<String> values = this.getUserValues(user);

        StringBuilder builder = new StringBuilder();
        builder.append("ecm:acl IN ('");
        builder.append(StringUtils.join(values, "', '"));
        builder.append("') ");

        if (user != null) {
            builder.append("OR (ecm:primaryType = 'Workspace' ");
            builder.append("AND ttcs:spaceMembers/*/login <> '").append(user).append("' ");
            builder.append("AND ttcs:visibility = '").append(WorkspaceType.PRIVATE).append("')");
        }

        return builder.toString();
    }


    /**
     * Get task actors.
     * 
     * @param user user
     * @return actors
     */
    private Set<String> getUserValues(String user) {
        // Values
        Set<String> values;

        if (StringUtils.isEmpty(user)) {
            values = new HashSet<>(1);
        } else {
            // User DN
            Name dn = this.personService.getEmptyPerson().buildDn(user);

            // Search user groups
            Group criteria = this.groupService.getEmptyGroup();
            criteria.setMembers(Arrays.asList(new Name[]{dn}));
            List<Group> groups = this.groupService.search(criteria);

            // Values
            values = new HashSet<>(groups.size() + 2);

            values.add(user);

            for (Group group : groups) {
                // Group CN
                String cn = group.getCn();

                values.add(cn);
            }
        }

        values.add("Everyone");

        return values;
    }

}
