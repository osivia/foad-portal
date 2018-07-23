package fr.gouv.education.foad.integrity.service;

import org.nuxeo.ecm.automation.client.Constants;
import org.nuxeo.ecm.automation.client.OperationRequest;
import org.nuxeo.ecm.automation.client.Session;
import org.osivia.services.workspace.portlet.repository.MemberManagementRepository;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import fr.toutatice.portail.cms.nuxeo.api.INuxeoCommand;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilter;
import fr.toutatice.portail.cms.nuxeo.api.NuxeoQueryFilterContext;

/**
 * Get invitations Nuxeo command.
 *
 * @author Cédric Krommenhoek
 * @see INuxeoCommand
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetProceduresInstancesCommand implements INuxeoCommand {

//    /** Workspace identifier. */
//    private final String workspaceId;
//    /** Model identifier. */
    private final String modelId;
    /** Invitation state. */
    private final String step;
    /** Invitation uid. */
    private final String uid;    
//    /** User identifiers. */
//    private final Set<String> identifiers;


//    /**
//     * Constructor.
//     *
//     * @param workspaceId workspace identifier
//     */
//    public GetProceduresInstancesCommand(String workspaceId) {
//        this(workspaceId, null, false);
//    }
//
//    /**
//     * Constructor.
//     *
//     * @param workspaceId workspace identifier
//     * @param request invitation request indicator
//     */
//    public GetProceduresInstancesCommand(String workspaceId, boolean request) {
//        this(workspaceId, null, request);
//    }
//
//    /**
//     * Constructor.
//     * 
//     * @param workspaceId workspace identifier
//     * @param invitationState invitation state
//     */
//    public GetProceduresInstancesCommand(String workspaceId, InvitationState invitationState) {
//        this(workspaceId, invitationState, false);
//    }
    
    

    /**
     * Constructor.
     * 
     * @param invitationState invitation state
     */
    public GetProceduresInstancesCommand(String step, String modelId, String uid) {

//        this.workspaceId = workspaceId;
//        if (request) {
//            this.modelId = MemberManagementRepository.REQUEST_MODEL_ID;
//        } else {
//            this.modelId = MemberManagementRepository.INVITATION_MODEL_ID;
//        }
        this.step = step;
        this.modelId = modelId;
        this.uid = uid;
//        this.identifiers = null;
    }
//
//
//    /**
//     * Constructor.
//     *
//     * @param workspaceId workspace identifier
//     * @param invitationState invitation state
//     * @param identifiers user identifiers
//     */
//    public GetProceduresInstancesCommand(String workspaceId, InvitationState invitationState, Set<String> identifiers) {
//        super();
//        this.workspaceId = workspaceId;
//        this.modelId = null;
//        this.invitationState = invitationState;
//        this.identifiers = identifiers;
//    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object execute(Session nuxeoSession) throws Exception {
        // Clause
        StringBuilder clause = new StringBuilder();
        clause.append("ecm:primaryType = 'ProcedureInstance' ");
//        if (this.workspaceId != null) {
//            clause.append("AND pi:globalVariablesValues.").append(MemberManagementRepository.WORKSPACE_IDENTIFIER_PROPERTY).append(" = '")
//                    .append(this.workspaceId).append("' ");
//        }
        if (modelId != null) {
            clause.append("AND pi:procedureModelWebId = 'procedure_"+modelId+"' ");
        }
        if (this.step != null) {
            clause.append("AND pi:currentStep = '")
                    .append(this.step).append("' ");
        }
        if (this.uid != null) {
            clause.append("AND pi:globalVariablesValues.").append(MemberManagementRepository.PERSON_UID_PROPERTY).append(" IN (");

            clause.append("'").append(uid).append("'");
            clause.append(") ORDER BY dc:created DESC");
        }


        // Filtered clause
        String filteredClause = NuxeoQueryFilter.addPublicationFilter(NuxeoQueryFilterContext.CONTEXT_LIVE, clause.toString());

        // Operation request
        OperationRequest request = nuxeoSession.newRequest("Document.QueryES");
        request.set(Constants.HEADER_NX_SCHEMAS, "dublincore, procedureInstance");
        request.set("query", "SELECT * FROM Document WHERE " + filteredClause);

        return request.execute();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getId() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.getClass().getName());
        builder.append("/");
//        if (this.workspaceId != null) {
//            builder.append(this.workspaceId);
//        }
//        builder.append("/");
//        if (this.modelId != null) {
//            builder.append(this.modelId);
//        }
//        builder.append("/");
        if (this.step != null) {
            builder.append(this.step);
        }
        builder.append("/");
//        if (this.identifiers != null) {
//            builder.append(StringUtils.join(this.identifiers, ","));
//        }
        return builder.toString();
    }

}
