package fr.gouv.education.foad.customizer.plugin.list;

import javax.portlet.PortletContext;

import fr.toutatice.portail.cms.nuxeo.api.portlet.PortletModule;

/**
 * Files list template module.
 * 
 * @author Cédric Krommenhoek
 * @see PortletModule
 */
public class FilesListTemplateModule extends PortletModule {

    /**
     * Constructor.
     * 
     * @param portletContext portlet context
     */
    public FilesListTemplateModule(PortletContext portletContext) {
        super(portletContext);
    }

}
