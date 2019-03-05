package fr.gouv.education.foad.portlet.service;

import org.osivia.services.workspace.portlet.model.AbstractChangeRoleForm;
import org.osivia.services.workspace.portlet.service.MemberManagementService;
import org.springframework.validation.Errors;

/**
 * Customized member management portlet service interface.
 * 
 * @author Cédric Krommenhoek
 */
public interface CustomizedMemberManagementService extends MemberManagementService {

    /**
     * Change role form validation.
     * 
     * @param form form
     * @param errors errors
     */
    void validateChangeRoleForm(AbstractChangeRoleForm<?> form, Errors errors);

}
