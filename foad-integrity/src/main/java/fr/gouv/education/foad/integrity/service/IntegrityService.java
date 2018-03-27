package fr.gouv.education.foad.integrity.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

/**
 * service interface.
 *
 * @author Loïc Billon
 */
public interface IntegrityService {
	
	void checkIntegrity(PortalControllerContext portalControllerContext, boolean repare) throws PortletException;

}
