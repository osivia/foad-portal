package fr.gouv.education.foad.customizer.plugin.fragment;

import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.osivia.portal.api.context.PortalControllerContext;
import fr.toutatice.portail.cms.nuxeo.portlets.fragment.PropertyFragmentModule;

/**
 * This fragment is used to filter display of property fragments based on the login pattern.
 * This fragment is not displayed when user name@tribu.local is logged.
 *  
 * @author Loïc Billon
 *
 */
public class DenyFromLocalAccountsFragment extends PropertyFragmentModule {

	private static final String TRIBU_LOCAL = "tribu.local";

	public DenyFromLocalAccountsFragment(PortletContext portletContext, boolean html) {
		super(portletContext, html);
	}

	@Override
	public void doView(PortalControllerContext portalControllerContext) throws PortletException {
		
		boolean showFragment = true;
		
		// Request
        PortletRequest request = portalControllerContext.getRequest();
        if(request != null && request.getUserPrincipal() != null) {
        	String login = request.getUserPrincipal().getName();
        	if(StringUtils.endsWith(login, TRIBU_LOCAL)) {
        		// Empty property value
                request.setAttribute("osivia.emptyResponse", "1");
                showFragment = false;
        	}
        }
        
        if(showFragment) {
    		super.doView(portalControllerContext);
        }
		
	}
}
