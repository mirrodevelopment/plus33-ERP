/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.dto
 * File              : InventoryDashboardResponse.java
 * Purpose           : Data Transfer Object for request/response in Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryDashboardController
 * Related Service   : InventoryDashboardService, InventoryDashboardServiceImpl
 * Related Repository: InventoryDashboardRepository
 * Related Entity    : InventoryDashboard
 * Related DTO       : InventoryAgingExpiryResponse, InventoryDashboardResponse, InventoryKpisResponse, InventoryReplenishmentResponse, InventoryTraceabilityResponse
 * Related Mapper    : InventoryDashboardMapper
 * Related DB Table  : inventory_dashboards
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryDashboardController, InventoryDashboardService, InventoryDashboardServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Analytics Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.analytics.dto;

public record InventoryDashboardResponse(
        InventoryKpisResponse kpis,
        InventoryAgingExpiryResponse agingExpiry,
        InventoryReplenishmentResponse replenishment,
        InventoryTraceabilityResponse traceability,
        InventoryTurnoverResponse turnover
) {}
