/**
 * 
 */
package fr.gouv.education.foad.accounts.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.naming.Name;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortalContext;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.jboss.portal.theme.render.renderer.ActionRendererContext;
import org.osivia.directory.v2.model.CollabProfile;
import org.osivia.directory.v2.model.ext.WorkspaceGroupType;
import org.osivia.directory.v2.model.ext.WorkspaceMember;
import org.osivia.directory.v2.service.PersonUpdateService;
import org.osivia.directory.v2.service.WorkspaceService;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.directory.v2.model.Person;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.notifications.NotificationsType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.context.PortletConfigAware;
import org.springframework.web.portlet.context.PortletContextAware;

import fr.toutatice.portail.cms.nuxeo.api.CMSPortlet;

/**
 * @author loic
 *
 */
@Controller
@RequestMapping(value = "VIEW")
public class AccountsController extends CMSPortlet implements PortletConfigAware, PortletContextAware{


    /** Portlet config. */
    private PortletConfig portletConfig;
    /** Portlet context. */
    private PortletContext portletContext;
    
    @Autowired
    private WorkspaceService workspaceService;    
    
	@Autowired
	private PersonUpdateService personService;

    /**
     * Post-construct.
     *
     * @throws PortletException
     */
    @PostConstruct
    public void postConstruct() throws PortletException {
        super.init(this.portletConfig);
    }


    /**
     * Pre-destroy.
     */
    @PreDestroy
    public void preDestroy() {
        super.destroy();
    }

    /**
     * View render mapping.
     *
     * @param request render request
     * @param response render response
     * @return view path
     */
    @RenderMapping
    public String view(RenderRequest request, RenderResponse response) {
        return "view";
    }

    @ModelAttribute("form")
    public AccountsForm getForm() {
    	return new AccountsForm();
    }
    
    @ActionMapping("migrateAccount")
    public void migrateAccount(@ModelAttribute("form") AccountsForm form, ActionRequest request, ActionResponse response) {
    	
    	PortalControllerContext pcc = new PortalControllerContext(portletContext, request, response);
    	Bundle bundle = getBundleFactory().getBundle(null);
    	
    	String oldAccountUid = form.getOldAccountUid();
		Person oldPerson = personService.getPerson(oldAccountUid);
    	if(oldPerson == null) {
    		// error
			getNotificationsService().addSimpleNotification(pcc, bundle.getString("account.mig.errornotfound"), NotificationsType.ERROR);

    	}
    	else {
	    	
	    	Person newPerson = personService.getPerson(form.getNewAccountUid());
	    	if(newPerson == null) {
	    		newPerson = personService.getEmptyPerson();
	    		newPerson.setUid(form.getNewAccountUid());
	    		newPerson.setMail(form.getNewAccountUid());
	    		newPerson.setSn(oldPerson.getSn());
	    		newPerson.setGivenName(oldPerson.getGivenName());
	    		newPerson.setCn(oldPerson.getCn());
	    		newPerson.setDisplayName(oldPerson.getDisplayName());
	    		personService.create(newPerson);
	    		
				getNotificationsService().addSimpleNotification(pcc, bundle.getString("account.mig.newAccount",
						form.getNewAccountUid()), NotificationsType.INFO);

	    	}
	    	
	    	CollabProfile profile = workspaceService.getEmptyProfile();
	    	List<Name> list = new ArrayList<Name>();
	    	list.add(oldPerson.getDn());
	    	profile.setUniqueMember(list);
	    	profile.setType(WorkspaceGroupType.space_group);
			List<CollabProfile> workspaces = workspaceService.findByCriteria(profile);
			
			Integer spaceCount = 0, localGroups = 0;
			for(CollabProfile workspaceProfile : workspaces) {
				
				WorkspaceMember member = workspaceService.getMember(workspaceProfile.getWorkspaceId(), oldAccountUid);
							
		    	workspaceService.addOrModifyMember(workspaceProfile.getWorkspaceId(), newPerson.getDn(), member.getRole());
		    	
		    	for(CollabProfile localGroup : member.getLocalGroups()) {
		    		workspaceService.addMemberToLocalGroup(workspaceProfile.getWorkspaceId(), localGroup.getDn(), newPerson.getDn());
		    		
		    		localGroups = localGroups +1;

		    	}
		    	
		    	// XXX: Pour l'instant on ne supprime pas l'ancienne affectation.
		    	//workspaceService.removeMember(workspaceProfile.getWorkspaceId(), oldPerson.getDn());
		    	
		    	spaceCount = spaceCount +1;
			}
			
			getNotificationsService().addSimpleNotification(pcc, bundle.getString("account.mig.success",
					spaceCount, localGroups), NotificationsType.SUCCESS);
		
    	}
    	
    }
    
	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.context.PortletContextAware#setPortletContext(javax.portlet.PortletContext)
	 */
	@Override
	public void setPortletContext(PortletContext portletContext) {
		this.portletContext = portletContext;
		
	}
	/* (non-Javadoc)
	 * @see org.springframework.web.portlet.context.PortletConfigAware#setPortletConfig(javax.portlet.PortletConfig)
	 */
	@Override
	public void setPortletConfig(PortletConfig portletConfig) {
		this.portletConfig = portletConfig;
		
	}

    
}
