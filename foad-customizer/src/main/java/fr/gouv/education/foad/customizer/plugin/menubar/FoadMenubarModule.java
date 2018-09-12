package fr.gouv.education.foad.customizer.plugin.menubar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.cms.impl.BasicPublicationInfos;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;

/**
 * FOAD menubar module.
 * 
 * @author Cédric Krommenhoek
 * @see MenubarModule
 */
public class FoadMenubarModule implements MenubarModule {

    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
            DocumentContext<? extends EcmDocument> spaceDocumentContext) throws PortalException {
        // Do nothing
    }


    /**
     * Suppression fonctionnalités socle : synchro Drive & Publication distante
     * Suppression types de fichiers : audio, video (redondant avec fichier), pad (obsolète avec Onlyoffice).
     * 
     * {@inheritDoc}
     */
    @Override
    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
            DocumentContext<? extends EcmDocument> documentContext) throws PortalException {
        Set<String> removedIdentifiers = new HashSet<>(Arrays.asList(new String[]{"SYNCHRONIZE_ACTION", "REMOTE_PUBLISHING_URL", 
        		"ADD_AUDIO", "ADD_VIDEO", "ADD_TOUTATICEPAD"}));

        BasicPublicationInfos publicationInfos = documentContext.getPublicationInfos(BasicPublicationInfos.class);
        if (publicationInfos.isDraft()) {
            removedIdentifiers.add("VALIDATION_WF_URL");
        }

        List<MenubarItem> removedItems = new ArrayList<>(removedIdentifiers.size());
        
        for (MenubarItem item : menubar) {
            if (removedIdentifiers.contains(item.getId())) {
                removedItems.add(item);
            }
        }

        for (MenubarItem item : removedItems) {
            menubar.remove(item);
        }
    }

}
