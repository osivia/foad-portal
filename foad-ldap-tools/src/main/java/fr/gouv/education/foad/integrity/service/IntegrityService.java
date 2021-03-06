package fr.gouv.education.foad.integrity.service;

import java.util.Date;
import java.util.List;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

import fr.gouv.education.foad.integrity.controller.DeleteDocForm;

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
	 * Remove a set of invitations
	 * 
	 * @param portalControllerContext
	 * @param boolean1
	 * @throws PortletException 
	 */
	Integer purgeInvit(PortalControllerContext portalControllerContext, boolean test) throws PortletException;
	
	
	/**
	 * Remove all expired invitations
	 * @param portalControllerContext
	 * @throws PortletException
	 */
	void purgeAllInvit(PortalControllerContext portalControllerContext) throws PortletException;


	/**
	 * Remove all users with no connexion date
	 * 
	 * @param portalControllerContext
	 * @param purge
	 * @throws PortletException 
	 */
	void purgeAllUsers(PortalControllerContext portalControllerContext, Boolean test) throws PortletException;
	
	/**
	 * Remove users with logins in argument
	 * 
	 * @param portalControllerContext
	 * @param purge
	 * @param logins
	 * @throws PortletException 
	 */
	int purgeUsers(PortalControllerContext portalControllerContext, List<String> logins, Boolean test) throws PortletException;

	/**
	 * @param portalControllerContext
	 * @param validity
	 * @param current 
	 * @param validityTest
	 */
	void chgValidDate(PortalControllerContext portalControllerContext, Date validity, Date current, Boolean validityTest);

	/**
	 * @param portalControllerContext
	 * @param validity
	 * @param logins
	 * @param validityTest
	 */
	void chgValidDate(PortalControllerContext portalControllerContext, Date validity, List<String> logins,
			Boolean validityTest);

	/**
	 * Migration : create staple for last modifications, migration PictureBooks in Folders
	 * @param portalControllerContext
	 */
	void updateWks(PortalControllerContext portalControllerContext);

	/**
	 * @param form
	 * @param portalControllerContext
	 */
	void deleteDoc(DeleteDocForm form, PortalControllerContext portalControllerContext);



}
