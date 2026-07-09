/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : PayrollCalendarType.java
 * Purpose           : Enumeration of typed constants for Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollCalendarTypeController
 * Related Service   : PayrollCalendarTypeService, PayrollCalendarTypeServiceImpl
 * Related Repository: PayrollCalendarTypeRepository
 * Related Entity    : PayrollCalendarType
 * Related DTO       : N/A
 * Related Mapper    : PayrollCalendarTypeMapper
 * Related DB Table  : payroll_calendar_types
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Workforce Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

public enum PayrollCalendarType {
    MONTHLY,
    BI_WEEKLY,
    WEEKLY,
    DAILY,
    OFF_CYCLE,
    FINAL_SETTLEMENT,
    BONUS_RUN,
    CORRECTION_RUN
}
