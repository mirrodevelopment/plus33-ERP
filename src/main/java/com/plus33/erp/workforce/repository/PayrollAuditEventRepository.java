/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : PayrollAuditEventRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollAuditEventController
 * Related Service   : PayrollAuditEventService, PayrollAuditEventServiceImpl
 * Related Repository: PayrollAuditEventRepository
 * Related Entity    : PayrollAuditEvent
 * Related DTO       : N/A
 * Related Mapper    : PayrollAuditEventMapper
 * Related DB Table  : payroll_audit_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollAuditEventService, PayrollAuditEventServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'payroll_audit_events' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollAuditEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollAuditEventRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'payroll_audit_events' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code payroll_audit_events}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PayrollAuditEventRepository extends JpaRepository<PayrollAuditEvent, Long> {
    List<PayrollAuditEvent> findByCompanyId(Long companyId);
    List<PayrollAuditEvent> findByPayrollRunId(Long payrollRunId);
}