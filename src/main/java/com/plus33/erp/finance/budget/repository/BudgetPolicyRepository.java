/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.repository
 * File              : BudgetPolicyRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetPolicyController
 * Related Service   : BudgetPolicyService, BudgetPolicyServiceImpl
 * Related Repository: BudgetPolicyRepository
 * Related Entity    : BudgetPolicy
 * Related DTO       : N/A
 * Related Mapper    : BudgetPolicyMapper
 * Related DB Table  : budget_policys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetPolicyService, BudgetPolicyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'budget_policys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetPolicyRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'budget_policys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_policys}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BudgetPolicyRepository extends JpaRepository<BudgetPolicy, Long> {
    Optional<BudgetPolicy> findByCompanyIdAndCode(Long companyId, String code);
}