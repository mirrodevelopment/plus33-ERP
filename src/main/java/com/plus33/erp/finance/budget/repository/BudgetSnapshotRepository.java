/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.repository
 * File              : BudgetSnapshotRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetSnapshotController
 * Related Service   : BudgetSnapshotService, BudgetSnapshotServiceImpl
 * Related Repository: BudgetSnapshotRepository
 * Related Entity    : BudgetSnapshot
 * Related DTO       : N/A
 * Related Mapper    : BudgetSnapshotMapper
 * Related DB Table  : budget_snapshots
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetSnapshotService, BudgetSnapshotServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'budget_snapshots' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetSnapshotRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'budget_snapshots' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_snapshots}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BudgetSnapshotRepository extends JpaRepository<BudgetSnapshot, Long> {
    List<BudgetSnapshot> findAllByBudgetIdOrderByVersionNumberDesc(Long budgetId);
}