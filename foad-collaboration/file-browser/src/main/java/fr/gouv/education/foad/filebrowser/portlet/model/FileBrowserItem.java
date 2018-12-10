package fr.gouv.education.foad.filebrowser.portlet.model;

import java.util.Date;

import org.osivia.portal.api.cms.impl.BasicPermissions;
import org.osivia.portal.api.cms.impl.BasicPublicationInfos;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.domain.DocumentDTO;

/**
 * File browser item java-bean.
 * 
 * @author Cédric Krommenhoek
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FileBrowserItem {

    /** Document DTO. */
    private DocumentDTO document;

    /** Title. */
    private String title;
    /** Last modification. */
    private Date lastModification;
    /** Last contributor. */
    private String lastContributor;
    /** File size, may be null. */
    private Long size;

    /** Folderish indicator. */
    private boolean folderish;

    /** Publication infos. */
    private BasicPublicationInfos publicationInfos;
    /** Permissions. */
    private BasicPermissions permissions;


    /**
     * Constructor.
     */
    public FileBrowserItem() {
        super();
    }


    /**
     * Getter for document.
     * 
     * @return the document
     */
    public DocumentDTO getDocument() {
        return document;
    }

    /**
     * Setter for document.
     * 
     * @param document the document to set
     */
    public void setDocument(DocumentDTO document) {
        this.document = document;
    }

    /**
     * Getter for title.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title.
     * 
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for lastModification.
     * 
     * @return the lastModification
     */
    public Date getLastModification() {
        return lastModification;
    }

    /**
     * Setter for lastModification.
     * 
     * @param lastModification the lastModification to set
     */
    public void setLastModification(Date lastModification) {
        this.lastModification = lastModification;
    }

    /**
     * Getter for lastContributor.
     * 
     * @return the lastContributor
     */
    public String getLastContributor() {
        return lastContributor;
    }

    /**
     * Setter for lastContributor.
     * 
     * @param lastContributor the lastContributor to set
     */
    public void setLastContributor(String lastContributor) {
        this.lastContributor = lastContributor;
    }

    /**
     * Getter for size.
     * 
     * @return the size
     */
    public Long getSize() {
        return size;
    }

    /**
     * Setter for size.
     * 
     * @param size the size to set
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
     * Getter for folderish.
     * 
     * @return the folderish
     */
    public boolean isFolderish() {
        return folderish;
    }

    /**
     * Setter for folderish.
     * 
     * @param folderish the folderish to set
     */
    public void setFolderish(boolean folderish) {
        this.folderish = folderish;
    }

    /**
     * Getter for publicationInfos.
     * 
     * @return the publicationInfos
     */
    public BasicPublicationInfos getPublicationInfos() {
        return publicationInfos;
    }

    /**
     * Setter for publicationInfos.
     * 
     * @param publicationInfos the publicationInfos to set
     */
    public void setPublicationInfos(BasicPublicationInfos publicationInfos) {
        this.publicationInfos = publicationInfos;
    }

    /**
     * Getter for permissions.
     * 
     * @return the permissions
     */
    public BasicPermissions getPermissions() {
        return permissions;
    }

    /**
     * Setter for permissions.
     * 
     * @param permissions the permissions to set
     */
    public void setPermissions(BasicPermissions permissions) {
        this.permissions = permissions;
    }

}
