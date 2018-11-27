package fr.gouv.education.foad.search.portlet.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

import fr.gouv.education.foad.search.portlet.model.TaskPath;

/**
 * Search portlet repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface SearchRepository {

    /**
     * Get base path.
     * 
     * @param portalControllerContext portal controller context
     * @return base path
     * @throws PortletException
     */
    String getBasePath(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Get search task path.
     * 
     * @param portalControllerContext portal controller context
     * @param basePath base path
     * @return path
     * @throws PortletException
     */
    TaskPath getSearchTaskPath(PortalControllerContext portalControllerContext, String basePath) throws PortletException;

}
