package fr.gouv.education.foad.selector.scope.portlet.repository;

import org.springframework.stereotype.Repository;

import fr.gouv.education.foad.common.repository.CommonRepositoryImpl;

/**
 * Scope selector portlet repository implementation.
 * 
 * @author Cédric Krommenhoek
 * @see CommonRepositoryImpl
 * @see ScopeSelectorRepository
 */
@Repository
public class ScopeSelectorRepositoryImpl extends CommonRepositoryImpl implements ScopeSelectorRepository {

    /**
     * Constructor.
     */
    public ScopeSelectorRepositoryImpl() {
        super();
    }

}
