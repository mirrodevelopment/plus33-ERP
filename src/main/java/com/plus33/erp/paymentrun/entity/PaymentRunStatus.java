/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.entity
 * File              : PaymentRunStatus.java
 * Purpose           : Enumeration of typed constants for Paymentrun Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentRunStatusController
 * Related Service   : PaymentRunStatusService, PaymentRunStatusServiceImpl
 * Related Repository: PaymentRunStatusRepository
 * Related Entity    : PaymentRunStatus
 * Related DTO       : N/A
 * Related Mapper    : PaymentRunStatusMapper
 * Related DB Table  : payment_run_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Paymentrun Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Paymentrun Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.paymentrun.entity;

public enum PaymentRunStatus {
    DRAFT,
    CALCULATED,
    APPROVED,
    PROCESSING,
    COMPLETED,
    CANCELLED
}
