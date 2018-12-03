package fr.gouv.education.foad.selector.scope.portlet.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.windows.PortalWindow;
import org.osivia.portal.api.windows.WindowFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.gouv.education.foad.selector.scope.portlet.model.ScopeSelectorForm;
import fr.gouv.education.foad.selector.scope.portlet.model.ScopeSelectorSettings;
import fr.gouv.education.foad.selector.scope.portlet.model.SearchScope;
import fr.gouv.education.foad.selector.scope.portlet.repository.ScopeSelectorRepository;
import fr.toutatice.portail.cms.nuxeo.api.PageSelectors;

/**
 * Scope selector portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see ScopeSelectorService
 */
@Service
public class ScopeSelectorServiceImpl implements ScopeSelectorService {

    /** Selector identifier window property. */
    private static final String SELECTOR_ID_WINDOW_PROPERTY = "foad.scope-selector.id";


    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private ScopeSelectorRepository repository;


    /**
     * Constructor.
     */
    public ScopeSelectorServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ScopeSelectorSettings getSettings(PortalControllerContext portalControllerContext) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());
        
        // Portlet settings
        ScopeSelectorSettings settings = this.applicationContext.getBean(ScopeSelectorSettings.class);
        
        // Selector identifier
        String selectorId = window.getProperty(SELECTOR_ID_WINDOW_PROPERTY);
        settings.setSelectorId(selectorId);
        
        return settings;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void save(PortalControllerContext portalControllerContext, ScopeSelectorSettings settings) throws PortletException {
        // Window
        PortalWindow window = WindowFactory.getWindow(portalControllerContext.getRequest());

        // Selector identifier
        String selectorId = StringUtils.trimToNull(settings.getSelectorId());
        window.setProperty(SELECTOR_ID_WINDOW_PROPERTY, selectorId);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public ScopeSelectorForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();

        // Selectors
        Map<String, List<String>> selectors = PageSelectors.decodeProperties(request.getParameter("selectors"));

        // Portlet settings
        ScopeSelectorSettings settings = this.getSettings(portalControllerContext);
        // Selector identifier
        String selectorId = settings.getSelectorId();
        // Selector values
        List<String> selectorValues = selectors.get(selectorId);
        
        // Empty selector indicator.
        boolean empty;
        if (CollectionUtils.isEmpty(selectorValues)) {
            empty = true;
        } else {
            // Selector value
            String selectorValue = selectorValues.get(0);

            empty = StringUtils.isEmpty(selectorValue);
        }


        // Form
        ScopeSelectorForm form = this.applicationContext.getBean(ScopeSelectorForm.class);

        // Search scope
        SearchScope scope;
        if (empty) {
            scope = SearchScope.GLOBAL;
        } else {
            scope = SearchScope.LOCAL;
        }
        form.setScope(scope);
        
        // Scopes
        List<SearchScope> scopes = Arrays.asList(SearchScope.values());
        form.setScopes(scopes);

        return form;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void select(PortalControllerContext portalControllerContext, ScopeSelectorForm form) throws PortletException {
        // Portlet request
        PortletRequest request = portalControllerContext.getRequest();
        // Action response
        ActionResponse response = (ActionResponse) portalControllerContext.getResponse();

        // Portlet settings
        ScopeSelectorSettings settings = this.getSettings(portalControllerContext);
        // Selector identifier
        String selectorId = settings.getSelectorId();

        if (StringUtils.isNotEmpty(selectorId)) {
            // Selectors
            Map<String, List<String>> selectors = PageSelectors.decodeProperties(request.getParameter("selectors"));

            // Search scope
            SearchScope scope = form.getScope();

            // Selector values
            if (SearchScope.LOCAL.equals(scope)) {
                // Root path
                String rootPath = this.repository.getRootPath(portalControllerContext);

                if (StringUtils.isEmpty(rootPath)) {
                    selectors.remove(selectorId);
                } else {
                    List<String> selectorValues = new ArrayList<>(1);
                    selectorValues.add(rootPath);
                    selectors.put(selectorId, selectorValues);
                }
            } else {
                selectors.remove(selectorId);
            }

            response.setRenderParameter("selectors", PageSelectors.encodeProperties(selectors));
        }
    }

}
