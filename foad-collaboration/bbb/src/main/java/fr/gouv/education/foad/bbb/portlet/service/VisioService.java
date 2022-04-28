package fr.gouv.education.foad.bbb.portlet.service;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

import fr.gouv.education.foad.bbb.portlet.form.VisioForm;

/**
 * 
 * @author Loïc Billon
 *
 */
public interface VisioService {

	VisioForm getForm(PortalControllerContext pcc) throws VisioException, PortletException;

}
