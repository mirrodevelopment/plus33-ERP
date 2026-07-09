/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.entity
 * File              : PurchaseOrderStatus.java
 * Purpose           : Enumeration of typed constants for Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseOrderStatusController
 * Related Service   : PurchaseOrderStatusService, PurchaseOrderStatusServiceImpl
 * Related Repository: PurchaseOrderStatusRepository
 * Related Entity    : PurchaseOrderStatus
 * Related DTO       : N/A
 * Related Mapper    : PurchaseOrderStatusMapper
 * Related DB Table  : purchase_order_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Procurement Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Procurement Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.procurement.entity;

public enum PurchaseOrderStatus {
    DRAFT,
    ISSUED,
    PARTIALLY_RECEIVED,
    RECEIVED,
    CLOSED,
    CANCELLED
}
