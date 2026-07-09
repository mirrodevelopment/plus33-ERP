/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : PayrollAuditTimelineService.java
 * Purpose           : Service interface contract defining the API for Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollAuditTimelineController
 * Related Service   : PayrollAuditTimelineService, PayrollAuditTimelineServiceImpl
 * Related Repository: PayrollAuditTimelineRepository
 * Related Entity    : PayrollAuditTimeline
 * Related DTO       : N/A
 * Related Mapper    : PayrollAuditTimelineMapper
 * Related DB Table  : payroll_audit_timelines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Workforce Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.workforce.service;

public interface PayrollAuditTimelineService {
    void logAuditEvent(Long companyId, Long payrollRunId, String eventType, String description, String actor);
}
