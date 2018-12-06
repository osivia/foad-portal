package fr.gouv.education.foad.filebrowser.plugin.model;

import java.util.Arrays;
import java.util.List;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.player.Player;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;

/**
 * File browser player module.
 * 
 * @author Cédric Krommenhoek
 * @see INuxeoPlayerModule
 */
@Component
public class FileBrowserPlayerModule implements INuxeoPlayerModule {

    /** Application context. */
    @Autowired
    private ApplicationContext applicationContext;


    /** File browser types. */
    private final List<String> types;


    /**
     * Constructor.
     */
    public FileBrowserPlayerModule() {
        super();

        // File browser types
        this.types = Arrays.asList("Folder", "OrderedFolder", "PictureBook");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Player getCMSPlayer(DocumentContext<Document> documentContext) {
        // Document
        Document document = documentContext.getDoc();

        // Player
        Player player;

        if ((document != null) && this.types.contains(document.getType())) {
            player = this.applicationContext.getBean(FileBrowserPlayer.class, document);
        } else {
            player = null;
        }

        return player;
    }

}
