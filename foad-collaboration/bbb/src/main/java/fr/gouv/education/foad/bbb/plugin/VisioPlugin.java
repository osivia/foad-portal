package fr.gouv.education.foad.bbb.plugin;

import java.util.List;
import java.util.Map;

import org.osivia.portal.api.cms.DocumentType;
import org.osivia.portal.api.customization.CustomizationContext;
import org.osivia.portal.api.player.IPlayerModule;
import org.osivia.portal.api.taskbar.TaskbarFactory;
import org.osivia.portal.api.taskbar.TaskbarItem;
import org.osivia.portal.api.taskbar.TaskbarItems;

import fr.toutatice.portail.cms.nuxeo.api.domain.AbstractPluginPortlet;

public class VisioPlugin extends AbstractPluginPortlet {

    private static final String PLUGIN_NAME = "bbb-plugin";
	
	@Override
	protected String getPluginName() {
        return PLUGIN_NAME;
	}

	@Override
	protected void customizeCMSProperties(String customizationId, CustomizationContext context) {
        // Document types
        this.customizeDocumentTypes(context);
        
        this.customizePlayers(context);
        // Taskbar items
        this.customizeTaskbarItems(context);        
	}

	private void customizeDocumentTypes(CustomizationContext context) {
        // Document types
        Map<String, DocumentType> types = this.getDocTypes(context);
        
        DocumentType visio = new DocumentType("Visio", false, true, true, false, true, false, null, null, "glyphicons glyphicons-call-video");
        types.put(visio.getName() ,visio);

	}
	

    private void customizePlayers(CustomizationContext context) {
        // Players
        List<IPlayerModule> players = getPlayers(context);
        players.add(new VisioPlayer());
    }
    
    /**
     * Customize taskbar items.
     *
     * @param context customization context
     */
    private void customizeTaskbarItems(CustomizationContext context) {
        // Taskbar items
        TaskbarItems items = this.getTaskbarItems(context);
        // Factory
        TaskbarFactory factory = this.getTaskbarService().getFactory();

        // Agenda
        TaskbarItem agenda = factory.createCmsTaskbarItem("VISIO", "VISIO_TASK", "glyphicons glyphicons-call-video", "Visio");
        agenda.setToDefault(3);
        items.add(agenda);
    }
    
}
