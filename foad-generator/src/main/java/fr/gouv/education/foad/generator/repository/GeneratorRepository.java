package fr.gouv.education.foad.generator.repository;

import javax.portlet.PortletException;

import org.osivia.portal.api.context.PortalControllerContext;

import fr.gouv.education.foad.generator.model.Configuration;
import fr.gouv.education.foad.generator.model.GenerateForm;

/**
 * Generator repository interface.
 *
 * @author Cédric Krommenhoek
 */
public interface GeneratorRepository {

    /**
     * Get generator configuration.
     *
     * @param portalControllerContext portal controller context
     * @return configuration
     * @throws PortletException
     */
    Configuration getConfiguration(PortalControllerContext portalControllerContext) throws PortletException;


    /**
     * Set generator configuration.
     *
     * @param portalControllerContext portal controller context
     * @param configuration generator configuration
     * @throws PortletException
     */
    void setConfiguration(PortalControllerContext portalControllerContext, Configuration configuration) throws PortletException;


    /**
     * Generate.
     *
     * @param portalControllerContext portal controller context
     * @param form 
     * @throws PortletException
     */
    void generate(PortalControllerContext portalControllerContext, GenerateForm form) throws PortletException;


    /**
     * Purge.
     * 
     * @param portalControllerContext portal controller context
     * @param form 
     * @throws PortletException
     */
    void purge(PortalControllerContext portalControllerContext, GenerateForm form) throws PortletException;

}
