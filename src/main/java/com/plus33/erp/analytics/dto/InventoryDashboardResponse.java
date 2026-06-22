package com.plus33.erp.analytics.dto;

public record InventoryDashboardResponse(
        InventoryKpisResponse kpis,
        InventoryAgingExpiryResponse agingExpiry,
        InventoryReplenishmentResponse replenishment,
        InventoryTraceabilityResponse traceability,
        InventoryTurnoverResponse turnover
) {}
