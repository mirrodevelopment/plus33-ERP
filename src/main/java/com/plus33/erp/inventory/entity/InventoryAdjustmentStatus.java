/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : InventoryAdjustmentStatus.java
 * Purpose           : Enumeration of typed constants for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAdjustmentStatusController
 * Related Service   : InventoryAdjustmentStatusService, InventoryAdjustmentStatusServiceImpl
 * Related Repository: InventoryAdjustmentStatusRepository
 * Related Entity    : InventoryAdjustmentStatus
 * Related DTO       : N/A
 * Related Mapper    : InventoryAdjustmentStatusMapper
 * Related DB Table  : inventory_adjustment_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Inventory Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

public enum InventoryAdjustmentStatus {
    DRAFT,
    SUBMITTED,
    APPROVED,
    POSTED,
    CANCELLED
}
