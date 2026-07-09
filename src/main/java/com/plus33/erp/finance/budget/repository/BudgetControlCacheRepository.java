/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.repository
 * File              : BudgetControlCacheRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetControlCacheController
 * Related Service   : BudgetControlCacheService, BudgetControlCacheServiceImpl
 * Related Repository: BudgetControlCacheRepository
 * Related Entity    : BudgetControlCache
 * Related DTO       : N/A
 * Related Mapper    : BudgetControlCacheMapper
 * Related DB Table  : budget_control_caches
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetControlCacheService, BudgetControlCacheServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'budget_control_caches' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetControlCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetControlCacheRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'budget_control_caches' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_control_caches}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BudgetControlCacheRepository extends JpaRepository<BudgetControlCache, Long> {
}