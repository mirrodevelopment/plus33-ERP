/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : InventoryAdjustmentType.java
 * Purpose           : Enumeration of typed constants for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAdjustmentTypeController
 * Related Service   : InventoryAdjustmentTypeService, InventoryAdjustmentTypeServiceImpl
 * Related Repository: InventoryAdjustmentTypeRepository
 * Related Entity    : InventoryAdjustmentType
 * Related DTO       : N/A
 * Related Mapper    : InventoryAdjustmentTypeMapper
 * Related DB Table  : inventory_adjustment_types
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Inventory Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

public enum InventoryAdjustmentType {
    DAMAGE,
    EXPIRED,
    SHRINKAGE,
    FOUND_STOCK,
    STOCK_COUNT_VARIANCE,
    MANUAL_CORRECTION
}
