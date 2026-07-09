/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.repository
 * File              : BudgetRevisionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetRevisionController
 * Related Service   : BudgetRevisionService, BudgetRevisionServiceImpl
 * Related Repository: BudgetRevisionRepository
 * Related Entity    : BudgetRevision
 * Related DTO       : N/A
 * Related Mapper    : BudgetRevisionMapper
 * Related DB Table  : budget_revisions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetRevisionService, BudgetRevisionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'budget_revisions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetRevisionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'budget_revisions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_revisions}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BudgetRevisionRepository extends JpaRepository<BudgetRevision, Long> {
    List<BudgetRevision> findAllByBudgetId(Long budgetId);
    List<BudgetRevision> findAllByBudgetLineId(Long budgetLineId);
}