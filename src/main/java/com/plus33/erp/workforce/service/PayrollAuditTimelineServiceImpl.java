/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : PayrollAuditTimelineServiceImpl.java
 * Purpose           : Business logic service layer for Workforce Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollAuditTimelineController
 * Related Service   : PayrollAuditTimelineServiceImpl
 * Related Repository: PayrollAuditEventRepository
 * Related Entity    : PayrollAuditTimeline
 * Related DTO       : N/A
 * Related Mapper    : PayrollAuditTimelineMapper
 * Related DB Table  : payroll_audit_timelines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollAuditTimelineController, PayrollAuditTimelineServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Workforce Module. Implements PayrollAuditTimelineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.PayrollAuditEvent;
import com.plus33.erp.workforce.repository.PayrollAuditEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollAuditTimelineServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Workforce Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PayrollAuditTimelineController
 *   --> PayrollAuditTimelineServiceImpl (this)
 *   --> Validate business rules
 *   --> PayrollAuditTimelineRepository (read/write 'payroll_audit_timelines')
 *   --> PayrollAuditTimelineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code payroll_audit_timelines}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PayrollAuditTimelineServiceImpl implements PayrollAuditTimelineService {

    private final PayrollAuditEventRepository auditEventRepository;

    public PayrollAuditTimelineServiceImpl(PayrollAuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    /**
     * Performs the logAuditEvent operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param payrollRunId the payrollRunId input value
     * @param eventType the eventType input value
     * @param description the description input value
     * @param actor the actor input value
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void logAuditEvent(Long companyId, Long payrollRunId, String eventType, String description, String actor) {
        PayrollAuditEvent event = new PayrollAuditEvent();
        event.setCompanyId(companyId);
        event.setPayrollRunId(payrollRunId);
        event.setEventType(eventType);
        event.setDescription(description);
        event.setActor(actor != null ? actor : "SYSTEM");
        auditEventRepository.save(event);
    }
}