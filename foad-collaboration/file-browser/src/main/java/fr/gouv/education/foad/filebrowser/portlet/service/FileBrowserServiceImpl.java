package fr.gouv.education.foad.filebrowser.portlet.service;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.collections.CollectionUtils;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.context.PortalControllerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import fr.gouv.education.foad.filebrowser.portlet.model.FileBrowserForm;
import fr.gouv.education.foad.filebrowser.portlet.model.FileBrowserItem;
import fr.gouv.education.foad.filebrowser.portlet.repository.FileBrowserRepository;

/**
 * File browser portlet service implementation.
 * 
 * @author Cédric Krommenhoek
 * @see FileBrowserService
 */
@Service
public class FileBrowserServiceImpl implements FileBrowserService {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;

    /** Portlet repository. */
    @Autowired
    private FileBrowserRepository repository;


    /**
     * Constructor.
     */
    public FileBrowserServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public FileBrowserForm getForm(PortalControllerContext portalControllerContext) throws PortletException {
        // Documents
        List<Document> documents = this.repository.getDocuments(portalControllerContext);


        // Form
        FileBrowserForm form = this.applicationContext.getBean(FileBrowserForm.class);

        // Items
        List<FileBrowserItem> items;
        if (CollectionUtils.isEmpty(documents)) {
            items = null;
        } else {
            items = new ArrayList<>(documents.size());

            for (Document document : documents) {
                FileBrowserItem item = this.createItem(portalControllerContext, document);
                items.add(item);
            }
        }
        form.setItems(items);
        
        return form;
    }


    /**
     * Create file browser item.
     * 
     * @param portalControllerContext portal controller context
     * @param document Nuxeo document
     * @return item
     * @throws PortletException
     */
    private FileBrowserItem createItem(PortalControllerContext portalControllerContext, Document document) throws PortletException {
        // Item
        FileBrowserItem item = this.applicationContext.getBean(FileBrowserItem.class);

        // Title
        String title = document.getTitle();
        item.setTitle(title);


        return item;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateMenubar(PortalControllerContext portalControllerContext) throws PortletException {
        this.repository.updateMenubar(portalControllerContext);
    }

}
