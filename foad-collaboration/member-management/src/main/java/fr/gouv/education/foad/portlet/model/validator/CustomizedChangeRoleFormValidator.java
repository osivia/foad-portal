package fr.gouv.education.foad.portlet.model.validator;

import org.osivia.services.workspace.portlet.model.AbstractChangeRoleForm;
import org.osivia.services.workspace.portlet.model.validator.ChangeRoleFormValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import fr.gouv.education.foad.portlet.service.CustomizedMemberManagementService;

/**
 * Customized change role form validator.
 * 
 * @author Cédric Krommenhoek
 * @see ChangeRoleFormValidator
 */
@Component
@Primary
public class CustomizedChangeRoleFormValidator extends ChangeRoleFormValidator {

    /** Portlet service. */
    @Autowired
    private CustomizedMemberManagementService service;


    /**
     * Constructor.
     */
    public CustomizedChangeRoleFormValidator() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void validate(Object target, Errors errors) {
        super.validate(target, errors);

        // Form
        AbstractChangeRoleForm<?> form = (AbstractChangeRoleForm<?>) target;

        this.service.validateChangeRoleForm(form, errors);
    }

}
