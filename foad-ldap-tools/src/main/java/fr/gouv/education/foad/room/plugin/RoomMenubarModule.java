/**
 * 
 */
package fr.gouv.education.foad.room.plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.BooleanUtils;
import org.jboss.portal.theme.impl.render.dynamic.DynaRenderOptions;
import org.nuxeo.ecm.automation.client.model.Document;
import org.osivia.portal.api.Constants;
import org.osivia.portal.api.PortalException;
import org.osivia.portal.api.cms.DocumentContext;
import org.osivia.portal.api.cms.EcmDocument;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.internationalization.IInternationalizationService;
import org.osivia.portal.api.locator.Locator;
import org.osivia.portal.api.menubar.IMenubarService;
import org.osivia.portal.api.menubar.MenubarContainer;
import org.osivia.portal.api.menubar.MenubarDropdown;
import org.osivia.portal.api.menubar.MenubarItem;
import org.osivia.portal.api.menubar.MenubarModule;
import org.osivia.portal.api.urls.IPortalUrlFactory;
import org.osivia.portal.core.constants.InternalConstants;

/**
 * @author loic
 *
 */
public class RoomMenubarModule implements MenubarModule {

	/** Menubar service. */
	private final IMenubarService menubarService;
	/** Portal URL factory. */
	private final IPortalUrlFactory portalUrlFactory;
	/** Bundle factory. */
	private final IBundleFactory bundleFactory;

	/**
	 * 
	 */
	public RoomMenubarModule() {
		super();

		// Menubar service
		this.menubarService = Locator.findMBean(IMenubarService.class, IMenubarService.MBEAN_NAME);
		// Portal URL factory
		this.portalUrlFactory = Locator.findMBean(IPortalUrlFactory.class, IPortalUrlFactory.MBEAN_NAME);
		// Bundle factory
		IInternationalizationService internationalizationService = Locator.findMBean(IInternationalizationService.class,
				IInternationalizationService.MBEAN_NAME);
		this.bundleFactory = internationalizationService.getBundleFactory(this.getClass().getClassLoader());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osivia.portal.api.menubar.MenubarModule#customizeSpace(org.osivia.portal.
	 * api.context.PortalControllerContext, java.util.List,
	 * org.osivia.portal.api.cms.DocumentContext)
	 */
	@Override
	public void customizeSpace(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
			DocumentContext<? extends EcmDocument> spaceDocumentContext) throws PortalException {

		if (spaceDocumentContext != null) {
			// Space document
			Document space = (Document) spaceDocumentContext.getDoc();
			if (space != null) {
				// Check type
				String type = space.getType();
				PortletRequest request = portalControllerContext.getRequest();
				
				if ("Workspace".equals(type) && request != null) {
					
					boolean isGlobalAdministrator = BooleanUtils.isTrue(
							(Boolean) request.getAttribute(InternalConstants.ADMINISTRATOR_INDICATOR_ATTRIBUTE_NAME));

					// Check permissions
					if (isGlobalAdministrator) {

						// HTTP servlet request
						HttpServletRequest servletRequest = portalControllerContext.getHttpServletRequest();
						// Bundle
						Bundle bundle = this.bundleFactory.getBundle(servletRequest.getLocale());

						// Window properties
						Map<String, String> properties = new HashMap<String, String>();
						properties.put("osivia.title", bundle.getString("ROOM_MIG_TITLE"));
						properties.put(DynaRenderOptions.PARTIAL_REFRESH_ENABLED, String.valueOf(true));
						properties.put("osivia.ajaxLink", "1");
						properties.put("osivia.back.reset", String.valueOf(true));
						properties.put("osivia.navigation.reset", String.valueOf(true));
						properties.put(Constants.WINDOW_PROP_URI, space.getPath());
						

						// Menubar item
						String id = "ROOM_MIG";
						String title = bundle.getString("ROOM_MIG_TITLE");
						String icon = "halflings halflings-wrench";
						MenubarContainer parent = this.menubarService.getDropdown(portalControllerContext,
								MenubarDropdown.CONFIGURATION_DROPDOWN_MENU_ID);
						int order = 4;
						String url = this.portalUrlFactory.getStartPortletUrl(portalControllerContext,
								"foad-room-instance", properties);
						String target = null;
						String onclick = null;
						String htmlClasses = null;

						MenubarItem menubarItem = new MenubarItem(id, title, icon, parent, order, url, target, onclick,
								htmlClasses);
						menubar.add(menubarItem);
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osivia.portal.api.menubar.MenubarModule#customizeDocument(org.osivia.
	 * portal.api.context.PortalControllerContext, java.util.List,
	 * org.osivia.portal.api.cms.DocumentContext)
	 */
	@Override
	public void customizeDocument(PortalControllerContext portalControllerContext, List<MenubarItem> menubar,
			DocumentContext<? extends EcmDocument> documentContext) throws PortalException {

	}

}
