/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.entity
 * File              : CustomerStatus.java
 * Purpose           : Enumeration of typed constants for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerStatusController
 * Related Service   : CustomerStatusService, CustomerStatusServiceImpl
 * Related Repository: CustomerStatusRepository
 * Related Entity    : CustomerStatus
 * Related DTO       : N/A
 * Related Mapper    : CustomerStatusMapper
 * Related DB Table  : customer_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Sales Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.sales.entity;

public enum CustomerStatus {
    ACTIVE,
    INACTIVE,
    ON_HOLD,
    BLOCKED
}
