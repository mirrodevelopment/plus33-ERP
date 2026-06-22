package com.plus33.erp.analytics.service;

import com.plus33.erp.analytics.dto.*;

import java.util.List;

public interface InventoryAnalyticsService {

    InventoryDashboardResponse getDashboard(Long companyId, Long warehouseId, Long storeId);

    InventoryKpisResponse getKpis(Long companyId, Long warehouseId, Long storeId);

    InventoryAgingExpiryResponse getAgingExpiry(Long companyId, Long warehouseId, Long storeId);

    List<InventoryAbcXyzResponse> getAbcXyz(Long companyId, Long warehouseId, Long storeId);

    List<InventorySlowDeadResponse> getSlowDead(Long companyId, Long warehouseId, Long storeId);

    InventoryTurnoverResponse getTurnover(Long companyId, Long warehouseId, Long storeId);

    InventoryReplenishmentResponse getReplenishmentMetrics(Long companyId, Long warehouseId, Long storeId);

    InventoryTraceabilityResponse getTraceabilityMetrics(Long companyId, Long warehouseId, Long storeId);

    List<AnalyticsHealthResponse> getHealth();

    void refreshAllViews();
}
