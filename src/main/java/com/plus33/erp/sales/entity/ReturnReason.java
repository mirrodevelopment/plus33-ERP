/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.entity
 * File              : ReturnReason.java
 * Purpose           : Enumeration of typed constants for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReturnReasonController
 * Related Service   : ReturnReasonService, ReturnReasonServiceImpl
 * Related Repository: ReturnReasonRepository
 * Related Entity    : ReturnReason
 * Related DTO       : N/A
 * Related Mapper    : ReturnReasonMapper
 * Related DB Table  : return_reasons
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Sales Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.sales.entity;

public enum ReturnReason {
    DAMAGED,
    WRONG_ITEM,
    CUSTOMER_DISSATISFACTION,
    WARRANTY,
    EXPIRED,
    OTHER
}
