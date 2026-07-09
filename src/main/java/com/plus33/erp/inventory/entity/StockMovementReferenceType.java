/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : StockMovementReferenceType.java
 * Purpose           : Enumeration of typed constants for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockMovementReferenceTypeController
 * Related Service   : StockMovementReferenceTypeService, StockMovementReferenceTypeServiceImpl
 * Related Repository: StockMovementReferenceTypeRepository
 * Related Entity    : StockMovementReferenceType
 * Related DTO       : N/A
 * Related Mapper    : StockMovementReferenceTypeMapper
 * Related DB Table  : stock_movement_reference_types
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Inventory Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

public enum StockMovementReferenceType {
    GOODS_RECEIPT,
    GOODS_RECEIPT_CANCEL,
    INVENTORY_TRANSFER,
    INVENTORY_ADJUSTMENT,
    PICK_LIST,
    SALES_ORDER,
    CUSTOMER_RETURN,
    MANUFACTURING_ISSUE,
    MANUFACTURING_RECEIPT
}
