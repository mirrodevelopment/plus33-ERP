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
