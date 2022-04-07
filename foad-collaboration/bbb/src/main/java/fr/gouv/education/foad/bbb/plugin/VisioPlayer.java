package fr.gouv.education.foad.bbb.plugin;

import java.util.HashMap;
import java.util.Map;

import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.player.Player;

import fr.toutatice.portail.cms.nuxeo.api.player.INuxeoPlayerModule;

public class VisioPlayer implements INuxeoPlayerModule {

	@Override
	public Player getCMSPlayer(DocumentContext<Document> docCtx) {
		
        // Document
        Document document = docCtx.getDoc();
		
        if ("Visio".equals(document.getType())) {
            // Window properties
            Map<String, String> windowProperties = new HashMap<String, String>();
            windowProperties.put(Constants.WINDOW_PROP_URI, document.getPath());

            Player props = new Player();
            props.setWindowProperties(windowProperties);
            props.setPortletInstance("foad-bbb-instance");

            return props;
        }
        else return null;
	}
	

}
