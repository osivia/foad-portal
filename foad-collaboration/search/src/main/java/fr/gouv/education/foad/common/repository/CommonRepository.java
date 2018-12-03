package fr.gouv.education.foad.common.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

/**
 * Common repository interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface CommonRepository {

    /**
     * Get root path.
     * 
     * @param portalControllerContext portal controller context
     * @return path
     * @throws PortletException
     */
    String getRootPath(PortalControllerContext portalControllerContext) throws PortletException;

}
