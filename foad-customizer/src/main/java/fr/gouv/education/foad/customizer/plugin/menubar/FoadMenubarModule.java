package fr.gouv.education.foad.customizer.plugin.menubar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections.CollectionUtils;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.cms.impl.BasicPublicationInfos;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.menubar.MenubarContainer;
import org.osivia.portal.api.menubar.MenubarDropdown;
import org.osivia.portal.api.menubar.MenubarGroup;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;

/**
 * FOAD menubar module.
 * 
 * @author Cédric Krommenhoek
 * @see MenubarModule
 */
public class FoadMenubarModule implements MenubarModule {

    /** Merged dropdown menu identifier. */
    private static final String MERGED_DROPDOWN_MENU_ID = "MERGED";


    /** Menubar service. */
    private final IMenubarService menubarService;


    /**
     * Constructor.
     */
    public FoadMenubarModule() {
        super();

        // Menubar service
        this.menubarService = Locator.findMBean(IMenubarService.class, IMenubarService.MBEAN_NAME);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
            DocumentContext<? extends EcmDocument> spaceDocumentContext) throws PortalException {
        this.hideConfigurationItems(portalControllerContext, menubar);
        this.mergeDropdownMenus(portalControllerContext, menubar);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
            DocumentContext<? extends EcmDocument> documentContext) throws PortalException {
        this.removeItems(menubar, documentContext);
        this.mergeDropdownMenus(portalControllerContext, menubar);
    }


    /**
     * Hide configuration menubar items.
     * 
     * @param portalControllerContext portal controller context
     * @param menubar menubar
     */
    private void hideConfigurationItems(PortalControllerContext portalControllerContext, List<MenubarItem> menubar) {
        // Configuration dropdown
        MenubarContainer dropdown = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.CONFIGURATION_DROPDOWN_MENU_ID);

        if (dropdown != null) {
            for (MenubarItem item : menubar) {
                MenubarContainer parent = item.getParent();
                if ((parent != null) && parent.equals(dropdown)) {
                    item.setVisible(false);
                }
            }
        }
    }


    /**
     * Remove menubar items.
     * 
     * @param menubar menubar
     * @param documentContext document context
     */
    private void removeItems(List<MenubarItem> menubar, DocumentContext<? extends EcmDocument> documentContext) {
        // Identifiers
        List<String> identifiers = Arrays.asList("SYNCHRONIZE_ACTION", "REMOTE_PUBLISHING_URL", "ADD_AUDIO", "ADD_VIDEO", "ADD_TOUTATICEPAD");
        BasicPublicationInfos publicationInfos = documentContext.getPublicationInfos(BasicPublicationInfos.class);
        if (publicationInfos.isDraft()) {
            identifiers.add("VALIDATION_WF_URL");
        }

        // Removed items
        List<MenubarItem> removedItems = new ArrayList<>(identifiers.size());
        for (MenubarItem item : menubar) {
            if (identifiers.contains(item.getId())) {
                removedItems.add(item);
            }
        }

        menubar.removeAll(removedItems);
    }


    /**
     * Merge dropdown menus.
     * 
     * @param portalControllerContext portal controller context
     * @param menubar menubar
     */
    private void mergeDropdownMenus(PortalControllerContext portalControllerContext, List<MenubarItem> menubar) {
        // Dropdowns
        Map<MenubarDropdown, List<MenubarItem>> dropdowns = new HashMap<>();

        // Refresh item
        MenubarItem refresh = null;

        for (MenubarItem item : menubar) {
            MenubarContainer parent = item.getParent();

            if ("REFRESH".equals(item.getId())) {
                refresh = item;
            } else if (parent instanceof MenubarDropdown) {
                MenubarDropdown dropdown = (MenubarDropdown) parent;

                List<MenubarItem> dropdownItems = dropdowns.get(dropdown);
                if (dropdownItems == null) {
                    dropdownItems = new ArrayList<>();
                    dropdowns.put(dropdown, dropdownItems);
                }

                dropdownItems.add(item);
            }
        }

        // Increments
        Map<MenubarDropdown, Integer> increments = new HashMap<>();
        // CMS edition dropdown
        MenubarDropdown edition = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.CMS_EDITION_DROPDOWN_MENU_ID);
        if (edition != null) {
            increments.put(edition, 1000);
        }
        // Share dropdown
        MenubarDropdown share = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.SHARE_DROPDOWN_MENU_ID);
        if (share != null) {
            increments.put(share, 2000);
        }
        // Other options dropdown
        MenubarDropdown otherOptions = this.menubarService.getDropdown(portalControllerContext, MenubarDropdown.OTHER_OPTIONS_DROPDOWN_MENU_ID);
        if (otherOptions != null) {
            increments.put(otherOptions, 3000);
        }

        // Merged dropdown
        MenubarDropdown merged = this.menubarService.getDropdown(portalControllerContext, MERGED_DROPDOWN_MENU_ID);
        if (merged == null) {
            merged = new MenubarDropdown(MERGED_DROPDOWN_MENU_ID, null, "glyphicons glyphicons-option-vertical", MenubarGroup.GENERIC, 40);
            merged.setReducible(false);
            this.menubarService.addDropdown(portalControllerContext, merged);
        }

        for (Entry<MenubarDropdown, Integer> entry : increments.entrySet()) {
            MenubarDropdown dropdown = entry.getKey();
            List<MenubarItem> items = dropdowns.get(dropdown);
            Integer increment = increments.get(dropdown);
            if (CollectionUtils.isNotEmpty(items) && (increment != null)) {
                // Header
                MenubarItem header = new MenubarItem(dropdown.getId() + "_HEADER", dropdown.getTitle(), merged, increment - 100, null);
                header.setState(true);
                header.setDivider(true);
                menubar.add(header);

                for (MenubarItem item : items) {
                    item.setParent(merged);
                    item.setOrder(item.getOrder() + increment);
                    item.setDivider(false);
                }
            }
        }

        // Update refresh item
        if (refresh != null) {
            refresh.setParent(merged);
            refresh.setOrder(4000);
            refresh.setDivider(true);
        }
    }

}
