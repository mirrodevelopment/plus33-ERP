/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : InventoryTraceEventType.java
 * Purpose           : Enumeration of typed constants for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTraceEventTypeController
 * Related Service   : InventoryTraceEventTypeService, InventoryTraceEventTypeServiceImpl
 * Related Repository: InventoryTraceEventTypeRepository
 * Related Entity    : InventoryTraceEventType
 * Related DTO       : N/A
 * Related Mapper    : InventoryTraceEventTypeMapper
 * Related DB Table  : inventory_trace_event_types
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Inventory Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

public enum InventoryTraceEventType {
    RECEIPT,
    TRANSFER_OUT,
    TRANSFER_IN,
    ADJUSTMENT,
    COUNT_VARIANCE,
    RECALL,
    SALE,
    EXPIRED
}
