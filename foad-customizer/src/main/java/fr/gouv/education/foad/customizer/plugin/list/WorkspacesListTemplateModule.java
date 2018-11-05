package fr.gouv.education.foad.customizer.plugin.list;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.portlet.PortletModule;
import fr.toutatice.portail.cms.nuxeo.api.workspace.WorkspaceType;

/**
 * Workspaces list template module.
 * 
 * @author Cédric Krommenhoek
 * @see PortletModule
 */
public class WorkspacesListTemplateModule extends PortletModule {

    /**
     * Constructor.
     * 
     * @param portletContext portlet context
     */
    public WorkspacesListTemplateModule(PortletContext portletContext) {
        super(portletContext);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    protected void doView(RenderRequest request, RenderResponse response, PortletContext portletContext) throws PortletException, IOException {
        // Workspaces
        List<?> workspaces = (List<?>) request.getAttribute("documents");

        for (Object object : workspaces) {
            // Workspace
            DocumentDTO workspace = (DocumentDTO) object;
            // Workspace properties
            Map<String, Object> properties = workspace.getProperties();

            // Workspace type
            String visibility = (String) properties.get("ttcs:visibility");
            if (StringUtils.isNotEmpty(visibility)) {
                WorkspaceType type = WorkspaceType.valueOf(visibility);
                properties.put("workspaceType", type);
            }
        }
    }

}
