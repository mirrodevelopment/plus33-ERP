/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : InventoryRecallStatus.java
 * Purpose           : Enumeration of typed constants for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryRecallStatusController
 * Related Service   : InventoryRecallStatusService, InventoryRecallStatusServiceImpl
 * Related Repository: InventoryRecallStatusRepository
 * Related Entity    : InventoryRecallStatus
 * Related DTO       : N/A
 * Related Mapper    : InventoryRecallStatusMapper
 * Related DB Table  : inventory_recall_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Inventory Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

public enum InventoryRecallStatus {
    ACTIVE,
    CLOSED,
    CANCELLED
}
