/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : PayrollApprovalWorkflowRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollApprovalWorkflowController
 * Related Service   : PayrollApprovalWorkflowService, PayrollApprovalWorkflowServiceImpl
 * Related Repository: PayrollApprovalWorkflowRepository
 * Related Entity    : PayrollApprovalWorkflow
 * Related DTO       : N/A
 * Related Mapper    : PayrollApprovalWorkflowMapper
 * Related DB Table  : payroll_approval_workflows
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollApprovalWorkflowService, PayrollApprovalWorkflowServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'payroll_approval_workflows' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollApprovalWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollApprovalWorkflowRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'payroll_approval_workflows' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code payroll_approval_workflows}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PayrollApprovalWorkflowRepository extends JpaRepository<PayrollApprovalWorkflow, Long> {
    List<PayrollApprovalWorkflow> findByPayrollRunIdOrderByStepNumberAsc(Long payrollRunId);
}