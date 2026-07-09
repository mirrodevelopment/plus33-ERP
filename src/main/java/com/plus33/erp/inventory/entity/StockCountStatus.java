/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : StockCountStatus.java
 * Purpose           : Enumeration of typed constants for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountStatusController
 * Related Service   : StockCountStatusService, StockCountStatusServiceImpl
 * Related Repository: StockCountStatusRepository
 * Related Entity    : StockCountStatus
 * Related DTO       : N/A
 * Related Mapper    : StockCountStatusMapper
 * Related DB Table  : stock_count_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Inventory Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

public enum StockCountStatus {
    DRAFT,
    ASSIGNED,
    IN_PROGRESS,
    SUBMITTED,
    REJECTED,
    APPROVED,
    POSTED,
    CLOSED
}
