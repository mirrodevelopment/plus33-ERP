/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.entity
 * File              : PaymentStatus.java
 * Purpose           : Enumeration of typed constants for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentStatusController
 * Related Service   : PaymentStatusService, PaymentStatusServiceImpl
 * Related Repository: PaymentStatusRepository
 * Related Entity    : PaymentStatus
 * Related DTO       : N/A
 * Related Mapper    : PaymentStatusMapper
 * Related DB Table  : payment_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Finance Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.finance.entity;

public enum PaymentStatus {
    PENDING,
    COMPLETED,
    CANCELLED
}
