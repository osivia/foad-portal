package fr.gouv.education.foad.integrity.service;

import java.util.Date;
import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

/**
 * service interface.
 *
 * @author Loïc Billon
 */
public interface IntegrityService {
	
	/**
	 * Check integrity between LDAP users/groups and workspaces data
	 * @param portalControllerContext
	 * @param repare
	 * @throws PortletException
	 */
	void checkIntegrity(PortalControllerContext portalControllerContext, boolean repare) throws PortletException;

	/**
	 * Remove users with no connexion date
	 * 
	 * @param portalControllerContext
	 * @param purge
	 */
	void purgeUsers(PortalControllerContext portalControllerContext, boolean purge);

	/**
	 * @param validity
	 * @param current 
	 * @param validityTest
	 */
	Integer chgValidDate(Date validity, Date current, Boolean validityTest);

	/**
	 * @param validity
	 * @param logins
	 * @param validityTest
	 */
	Integer chgValidDate(Date validity, List<String> logins, Boolean validityTest);

}
