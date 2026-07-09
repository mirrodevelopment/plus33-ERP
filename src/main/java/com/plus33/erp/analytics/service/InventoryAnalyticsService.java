/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.service
 * File              : InventoryAnalyticsService.java
 * Purpose           : Service interface contract defining the API for Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAnalyticsController
 * Related Service   : InventoryAnalyticsService, InventoryAnalyticsServiceImpl
 * Related Repository: InventoryAnalyticsRepository
 * Related Entity    : InventoryAnalytics
 * Related DTO       : AnalyticsHealthResponse, InventoryAbcXyzResponse, InventoryAgingExpiryResponse, InventoryDashboardResponse, InventoryKpisResponse
 * Related Mapper    : InventoryAnalyticsMapper
 * Related DB Table  : inventory_analyticss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Analytics Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Analytics Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.analytics.service;

import com.plus33.erp.analytics.dto.*;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Analytics Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryAnalyticsService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.analytics.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Analytics Module.</p>
 *
 * <p><b>Module Deps      :</b> Analytics</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
