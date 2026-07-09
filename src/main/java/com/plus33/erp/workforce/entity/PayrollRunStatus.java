/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : PayrollRunStatus.java
 * Purpose           : Enumeration of typed constants for Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollRunStatusController
 * Related Service   : PayrollRunStatusService, PayrollRunStatusServiceImpl
 * Related Repository: PayrollRunStatusRepository
 * Related Entity    : PayrollRunStatus
 * Related DTO       : N/A
 * Related Mapper    : PayrollRunStatusMapper
 * Related DB Table  : payroll_run_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Workforce Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

public enum PayrollRunStatus {
    DRAFT,
    CALCULATED,
    VALIDATED,
    APPROVED,
    POSTED,
    PAID,
    ARCHIVED,
    REVERSED
}
