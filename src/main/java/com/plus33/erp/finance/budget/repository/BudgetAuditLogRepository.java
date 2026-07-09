/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.repository
 * File              : BudgetAuditLogRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetAuditLogController
 * Related Service   : BudgetAuditLogService, BudgetAuditLogServiceImpl
 * Related Repository: BudgetAuditLogRepository
 * Related Entity    : BudgetAuditLog
 * Related DTO       : N/A
 * Related Mapper    : BudgetAuditLogMapper
 * Related DB Table  : budget_audit_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetAuditLogService, BudgetAuditLogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'budget_audit_logs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetAuditLogRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'budget_audit_logs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_audit_logs}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BudgetAuditLogRepository extends JpaRepository<BudgetAuditLog, Long> {
    List<BudgetAuditLog> findAllByBudgetIdOrderByCreatedAtDesc(Long budgetId);
}