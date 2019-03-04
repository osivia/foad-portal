package fr.gouv.education.foad.portlet.service;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletException;

import org.apache.commons.lang.StringUtils;
import org.osivia.directory.v2.model.ext.WorkspaceRole;
import org.osivia.portal.api.context.PortalControllerContext;
import org.osivia.portal.api.internationalization.Bundle;
import org.osivia.portal.api.internationalization.IBundleFactory;
import org.osivia.portal.api.notifications.INotificationsService;
import org.osivia.portal.api.notifications.NotificationsType;
import org.osivia.services.workspace.portlet.model.Invitation;
import org.osivia.services.workspace.portlet.model.InvitationEditionForm;
import org.osivia.services.workspace.portlet.model.InvitationRequest;
import org.osivia.services.workspace.portlet.model.InvitationRequestsForm;
import org.osivia.services.workspace.portlet.model.InvitationsCreationForm;
import org.osivia.services.workspace.portlet.model.InvitationsForm;
import org.osivia.services.workspace.portlet.model.MemberManagementOptions;
import org.osivia.services.workspace.portlet.service.MemberManagementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

/**
 * Customized member management portlet service implementation.
 * Check of local accounts, they shouldn't be owners.
 * 
 * @author Loïc Billon
 * @author Cédric Krommenhoek
 * @see MemberManagementServiceImpl
 */
@Service
@Primary
public class CustomizedMemberManagementServiceImpl extends MemberManagementServiceImpl {

    /** Tribu local identifier suffix. */
    private static final String TRIBU_LOCAL = "@tribu.local";


    /** Bundle factory. */
    @Autowired
    private IBundleFactory bundleFactory;

    /** Notifications service. */
    @Autowired
    private INotificationsService notificationsService;


    /**
     * Constructor.
     */
    public CustomizedMemberManagementServiceImpl() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateInvitation(PortalControllerContext portalControllerContext, InvitationEditionForm form) throws PortletException {
        List<String> identifiers = new ArrayList<>();
        if (form.getInvitation().getRole().equals(WorkspaceRole.OWNER)) {
            identifiers.add(form.getInvitation().getId());
        }

        boolean localAccountsOwner = checkLocalAccounts(portalControllerContext, identifiers);

        if (!localAccountsOwner) {
            super.updateInvitation(portalControllerContext, form);
        } else {
            // Update model
            this.invalidateLoadedForms();
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createInvitations(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationsForm invitationsForm,
            InvitationsCreationForm creationForm) throws PortletException {
        List<String> identifiers = new ArrayList<>();
        if (creationForm.getRole().equals(WorkspaceRole.OWNER)) {
            for (Invitation invit : creationForm.getPendingInvitations()) {
                identifiers.add(invit.getId());
            }
        }

        boolean localAccountsOwner = checkLocalAccounts(portalControllerContext, identifiers);

        if (!localAccountsOwner) {
            super.createInvitations(portalControllerContext, options, invitationsForm, creationForm);
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void updateInvitationRequests(PortalControllerContext portalControllerContext, MemberManagementOptions options, InvitationRequestsForm form)
            throws PortletException {
        List<String> identifiers = new ArrayList<>();
        for (InvitationRequest member : form.getRequests()) {
            if (member.getRole().equals(WorkspaceRole.OWNER)) {
                identifiers.add(member.getId());
            }
        }

        boolean localAccountsOwner = checkLocalAccounts(portalControllerContext, identifiers);

        if (!localAccountsOwner) {
            super.updateInvitationRequests(portalControllerContext, options, form);
        } else {
            this.invalidateLoadedForms();
            this.getInvitationRequestsForm(portalControllerContext);
        }
    }


    /**
     * Main check based on pattern id "xxxx@tribu.local".
     * If validation fails, we fire a notification.
     * 
     * @param portalControllerContext portal controller context
     * @param identifiers user identifiers
     * @return true if an identifier has the pattern
     */
    private boolean checkLocalAccounts(PortalControllerContext portalControllerContext, List<String> identifiers) {
        boolean localAccounsAreOwner = false;
        String localAccountsIds = "";

        for (String identifier : identifiers) {
            if (StringUtils.endsWith(identifier, TRIBU_LOCAL)) {
                localAccounsAreOwner = true;

                if (StringUtils.isNotBlank(localAccountsIds)) {
                    localAccountsIds = localAccountsIds + ", ";
                }

                localAccountsIds = localAccountsIds + identifier;
            }
        }

        if (localAccounsAreOwner) {
            // Bundle
            Bundle bundle = bundleFactory.getBundle(portalControllerContext.getRequest().getLocale());
            // Notification
            String message = bundle.getString("MESSAGE_WORKSPACE_MEMBERS_UPDATE_ERROR_LOCALACCOUNTS", localAccountsIds);
            notificationsService.addSimpleNotification(portalControllerContext, message, NotificationsType.WARNING);
        }

        return localAccounsAreOwner;
    }

}
