/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : PlannedOrderStatus.java
 * Purpose           : Enumeration of typed constants for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlannedOrderStatusController
 * Related Service   : PlannedOrderStatusService, PlannedOrderStatusServiceImpl
 * Related Repository: PlannedOrderStatusRepository
 * Related Entity    : PlannedOrderStatus
 * Related DTO       : N/A
 * Related Mapper    : PlannedOrderStatusMapper
 * Related DB Table  : planned_order_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Manufacturing Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

public enum PlannedOrderStatus {
    OPEN,
    FIRMED,
    RELEASED,
    CANCELLED
}
