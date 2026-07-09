/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.repository
 * File              : BudgetVersionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetVersionController
 * Related Service   : BudgetVersionService, BudgetVersionServiceImpl
 * Related Repository: BudgetVersionRepository
 * Related Entity    : BudgetVersion
 * Related DTO       : N/A
 * Related Mapper    : BudgetVersionMapper
 * Related DB Table  : budget_versions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetVersionService, BudgetVersionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'budget_versions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetVersionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'budget_versions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_versions}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BudgetVersionRepository extends JpaRepository<BudgetVersion, Long> {
    Optional<BudgetVersion> findByBudgetIdAndVersionCode(Long budgetId, String versionCode);
    List<BudgetVersion> findAllByBudgetId(Long budgetId);
}