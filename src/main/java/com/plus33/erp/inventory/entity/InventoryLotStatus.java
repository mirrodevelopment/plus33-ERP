/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : InventoryLotStatus.java
 * Purpose           : Enumeration of typed constants for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryLotStatusController
 * Related Service   : InventoryLotStatusService, InventoryLotStatusServiceImpl
 * Related Repository: InventoryLotStatusRepository
 * Related Entity    : InventoryLotStatus
 * Related DTO       : N/A
 * Related Mapper    : InventoryLotStatusMapper
 * Related DB Table  : inventory_lot_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Inventory Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

public enum InventoryLotStatus {
    ACTIVE,
    QUARANTINED,
    RECALLED,
    EXPIRED
}
