/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : ProductionOrderStatus.java
 * Purpose           : Enumeration of typed constants for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionOrderStatusController
 * Related Service   : ProductionOrderStatusService, ProductionOrderStatusServiceImpl
 * Related Repository: ProductionOrderStatusRepository
 * Related Entity    : ProductionOrderStatus
 * Related DTO       : N/A
 * Related Mapper    : ProductionOrderStatusMapper
 * Related DB Table  : production_order_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Manufacturing Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

/**
 * Full production order state machine.
 *
 * Normal lifecycle:
 *   DRAFT → PLANNED → RELEASED → MATERIAL_ALLOCATED → IN_PROGRESS
 *        → PARTIALLY_COMPLETED → COMPLETED → QUALITY_PENDING → CLOSED
 *
 * Exception states:
 *   HOLD, CANCELLED, SCRAPPED, REWORK, REVERSED
 */
public enum ProductionOrderStatus {
    DRAFT,
    PLANNED,
    RELEASED,
    MATERIAL_ALLOCATED,
    IN_PROGRESS,
    PARTIALLY_COMPLETED,
    COMPLETED,
    QUALITY_PENDING,
    CLOSED,
    // Exception states
    HOLD,
    CANCELLED,
    SCRAPPED,
    REWORK,
    REVERSED
}
