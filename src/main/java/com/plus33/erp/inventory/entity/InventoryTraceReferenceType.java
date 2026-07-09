/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : InventoryTraceReferenceType.java
 * Purpose           : Enumeration of typed constants for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTraceReferenceTypeController
 * Related Service   : InventoryTraceReferenceTypeService, InventoryTraceReferenceTypeServiceImpl
 * Related Repository: InventoryTraceReferenceTypeRepository
 * Related Entity    : InventoryTraceReferenceType
 * Related DTO       : N/A
 * Related Mapper    : InventoryTraceReferenceTypeMapper
 * Related DB Table  : inventory_trace_reference_types
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Inventory Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

public enum InventoryTraceReferenceType {
    GOODS_RECEIPT,
    INVENTORY_TRANSFER,
    INVENTORY_ADJUSTMENT,
    STOCK_COUNT,
    SALES_ORDER,
    INVENTORY_RECALL,
    CUSTOMER_RETURN
}
