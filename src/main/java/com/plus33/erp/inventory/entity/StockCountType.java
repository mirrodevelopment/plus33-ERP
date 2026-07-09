/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : StockCountType.java
 * Purpose           : Enumeration of typed constants for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountTypeController
 * Related Service   : StockCountTypeService, StockCountTypeServiceImpl
 * Related Repository: StockCountTypeRepository
 * Related Entity    : StockCountType
 * Related DTO       : N/A
 * Related Mapper    : StockCountTypeMapper
 * Related DB Table  : stock_count_types
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Inventory Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

public enum StockCountType {
    FULL,
    CYCLE
}
