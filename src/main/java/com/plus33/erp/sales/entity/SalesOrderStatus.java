/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.entity
 * File              : SalesOrderStatus.java
 * Purpose           : Enumeration of typed constants for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesOrderStatusController
 * Related Service   : SalesOrderStatusService, SalesOrderStatusServiceImpl
 * Related Repository: SalesOrderStatusRepository
 * Related Entity    : SalesOrderStatus
 * Related DTO       : N/A
 * Related Mapper    : SalesOrderStatusMapper
 * Related DB Table  : sales_order_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Sales Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.sales.entity;

public enum SalesOrderStatus {
    DRAFT,
    SUBMITTED,
    APPROVED,
    PARTIALLY_FULFILLED,
    FULFILLED,
    INVOICED,
    CLOSED,
    CANCELLED
}
