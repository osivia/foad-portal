package fr.gouv.education.foad.customizer.plugin.fragment;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.apache.commons.lang.StringUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.urls.Link;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;

import fr.toutatice.portail.cms.nuxeo.api.NuxeoController;
import fr.toutatice.portail.cms.nuxeo.api.cms.NuxeoDocumentContext;
import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;
import fr.toutatice.portail.cms.nuxeo.api.fragment.FragmentModule;
import fr.toutatice.portail.cms.nuxeo.api.services.dao.DocumentDAO;

/**
 * Tile fragment module.
 * 
 * @author Cédric Krommenhoek
 * @see FragmentModule
 */
public class TileFragmentModule extends FragmentModule {

    /** Fragment identifier. */
    public static final String ID = "tile";


    /** JSP name. */
    private static final String JSP_NAME = "tile";

    /** Path property. */
    private static final String PATH = "path";
    /** Target property. */
    private static final String TARGET = "target";
    /** Properties. */
    private static final List<String> PROPERTIES = Arrays.asList(PATH, TARGET);


    /** Document DAO. */
    private final DocumentDAO documentDao;


    /**
     * Constructor.
     * 
     * @param portletContext portlet context
     */
    public TileFragmentModule(PortletContext portletContext) {
        super(portletContext);

        // Document DAO
        this.documentDao = DocumentDAO.getInstance();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void doView(RenderRequest request, RenderResponse response, PortletContext portletContext) throws PortletException, IOException {
        // Portal controller context
        PortalControllerContext portalControllerContext = new PortalControllerContext(portletContext, request, response);
        // Nuxeo controller
        NuxeoController nuxeoController = new NuxeoController(portalControllerContext);

        // Window
        PortalWindow window = WindowFactory.getWindow(request);


        // Document
        String path = window.getProperty(PATH);
        if (StringUtils.isNotEmpty(path)) {
            // Computed path
            path = nuxeoController.getComputedPath(path);

            // Nuxeo document
            NuxeoDocumentContext documentContext = nuxeoController.getDocumentContext(path);
            Document nuxeoDocument = documentContext.getDoc();

            // Document DTO
            DocumentDTO documentDto = this.documentDao.toDTO(nuxeoDocument);
            request.setAttribute("document", documentDto);
        }


        // Target link
        String target = window.getProperty(TARGET);
        if (StringUtils.isNotEmpty(target)) {
            // URL
            String url;
            if (!StringUtils.containsAny(target, "/.")) {
                // Convert webId to Nuxeo relative URL
                url = "/nuxeo/web/" + target;
            } else {
                // Relative or absolute URL
                url = target;
            }

            Link targetLink = nuxeoController.getLinkFromNuxeoURL(url);
            request.setAttribute("targetLink", targetLink);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void doAdmin(RenderRequest request, RenderResponse response, PortletContext portletContext) throws PortletException, IOException {
        // Window
        PortalWindow window = WindowFactory.getWindow(request);

        // Properties
        for (String property : PROPERTIES) {
            String value = window.getProperty(property);
            request.setAttribute(property, value);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void processAction(ActionRequest request, ActionResponse response, PortletContext portletContext) throws PortletException, IOException {
        if ("admin".equals(request.getPortletMode().toString()) && "save".equals(request.getParameter(ActionRequest.ACTION_NAME))) {
            // Current window
            PortalWindow window = WindowFactory.getWindow(request);

            // Properties
            for (String property : PROPERTIES) {
                String value = StringUtils.trimToNull(request.getParameter(property));
                window.setProperty(property, value);
            }
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getViewJSPName() {
        return JSP_NAME;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getAdminJSPName() {
        return JSP_NAME;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDisplayedInAdmin() {
        return true;
    }

}
