/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.controller
 * File              : InventoryAnalyticsController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAnalyticsController
 * Related Service   : InventoryAnalyticsControllerService, InventoryAnalyticsControllerServiceImpl
 * Related Repository: InventoryAnalyticsControllerRepository
 * Related Entity    : InventoryAnalyticsController
 * Related DTO       : AnalyticsHealthResponse, ApiResponse, InventoryAbcXyzResponse, InventoryAgingExpiryResponse, InventoryDashboardResponse
 * Related Mapper    : InventoryAnalyticsControllerMapper
 * Related DB Table  : inventory_analytics_controllers
 * Related REST APIs : GET /api/v1/analytics/inventory/dashboard, GET /api/v1/analytics/inventory/kpis, GET /api/v1/analytics/inventory/aging-expiry, GET /api/v1/analytics/inventory/abc-xyz
 * Depends On        : Common Module
 * Used By           : Analytics Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Analytics Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: GET /api/v1/analytics/inventory/dashboard, GET /api/v1/analytics/inventory/kpis, GET /api/v1/analytics/inventory/aging-expiry, GET /api/v1/analytics/inventory/abc-xyz
 ******************************************************************************/
package com.plus33.erp.analytics.controller;

import com.plus33.erp.analytics.dto.*;
import com.plus33.erp.analytics.service.InventoryAnalyticsService;
import com.plus33.erp.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Analytics Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryAnalyticsController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.analytics.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to InventoryAnalyticsService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> InventoryAnalyticsController.endpoint()
 *   --> InventoryAnalyticsService.method()
 *   --> InventoryAnalyticsRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> GET /api/v1/analytics/inventory/dashboard, GET /api/v1/analytics/inventory/kpis, GET /api/v1/analytics/inventory/aging-expiry, GET /api/v1/analytics/inventory/abc-xyz, GET /api/v1/analytics/inventory/slow-dead</p>
 * <p><b>Module Deps      :</b> Analytics, Common</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/analytics/inventory")
@Tag(name = "Inventory Analytics", description = "REST APIs for inventory dashboards, KPIs, and refresh monitoring")
public class InventoryAnalyticsController {

    private final InventoryAnalyticsService inventoryAnalyticsService;

    public InventoryAnalyticsController(InventoryAnalyticsService inventoryAnalyticsService) {
        this.inventoryAnalyticsService = inventoryAnalyticsService;
    }

    /**
     * Retrieves dashboard data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('INVENTORY_ANALYTICS_VIEW')")
    @Operation(summary = "Get combined inventory dashboard summary metrics")
    public ResponseEntity<ApiResponse<InventoryDashboardResponse>> getDashboard(
            @RequestParam Long companyId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long storeId
    ) {
        InventoryDashboardResponse response = inventoryAnalyticsService.getDashboard(companyId, warehouseId, storeId);
        return ResponseEntity.ok(ApiResponse.success("Inventory dashboard summary retrieved successfully", response));
    }

    /**
     * Retrieves kpis data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/kpis")
    @PreAuthorize("hasAuthority('INVENTORY_ANALYTICS_VIEW')")
    @Operation(summary = "Get inventory KPI metrics")
    public ResponseEntity<ApiResponse<InventoryKpisResponse>> getKpis(
            @RequestParam Long companyId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long storeId
    ) {
        InventoryKpisResponse response = inventoryAnalyticsService.getKpis(companyId, warehouseId, storeId);
        return ResponseEntity.ok(ApiResponse.success("Inventory KPIs retrieved successfully", response));
    }

    /**
     * Retrieves aging expiry data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/aging-expiry")
    @PreAuthorize("hasAuthority('INVENTORY_ANALYTICS_VIEW')")
    @Operation(summary = "Get inventory aging and expiry metrics")
    public ResponseEntity<ApiResponse<InventoryAgingExpiryResponse>> getAgingExpiry(
            @RequestParam Long companyId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long storeId
    ) {
        InventoryAgingExpiryResponse response = inventoryAnalyticsService.getAgingExpiry(companyId, warehouseId, storeId);
        return ResponseEntity.ok(ApiResponse.success("Inventory aging and expiry metrics retrieved successfully", response));
    }

    /**
     * Retrieves abc xyz data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/abc-xyz")
    @PreAuthorize("hasAuthority('INVENTORY_ANALYTICS_VIEW')")
    @Operation(summary = "Get inventory ABC/XYZ classifications")
    public ResponseEntity<ApiResponse<List<InventoryAbcXyzResponse>>> getAbcXyz(
            @RequestParam Long companyId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long storeId
    ) {
        List<InventoryAbcXyzResponse> response = inventoryAnalyticsService.getAbcXyz(companyId, warehouseId, storeId);
        return ResponseEntity.ok(ApiResponse.success("Inventory ABC/XYZ classifications retrieved successfully", response));
    }

    /**
     * Retrieves slow dead data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/slow-dead")
    @PreAuthorize("hasAuthority('INVENTORY_ANALYTICS_VIEW')")
    @Operation(summary = "Get inventory slow and dead stock details")
    public ResponseEntity<ApiResponse<List<InventorySlowDeadResponse>>> getSlowDead(
            @RequestParam Long companyId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long storeId
    ) {
        List<InventorySlowDeadResponse> response = inventoryAnalyticsService.getSlowDead(companyId, warehouseId, storeId);
        return ResponseEntity.ok(ApiResponse.success("Inventory slow and dead stock details retrieved successfully", response));
    }

    /**
     * Retrieves turnover data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/turnover")
    @PreAuthorize("hasAuthority('INVENTORY_ANALYTICS_VIEW')")
    @Operation(summary = "Get inventory turnover and days on hand metrics")
    public ResponseEntity<ApiResponse<InventoryTurnoverResponse>> getTurnover(
            @RequestParam Long companyId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long storeId
    ) {
        InventoryTurnoverResponse response = inventoryAnalyticsService.getTurnover(companyId, warehouseId, storeId);
        return ResponseEntity.ok(ApiResponse.success("Inventory turnover metrics retrieved successfully", response));
    }

    /**
     * Retrieves replenishment metrics data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/replenishment-metrics")
    @PreAuthorize("hasAuthority('INVENTORY_ANALYTICS_VIEW')")
    @Operation(summary = "Get inventory replenishment rule coverage and stats")
    public ResponseEntity<ApiResponse<InventoryReplenishmentResponse>> getReplenishmentMetrics(
            @RequestParam Long companyId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long storeId
    ) {
        InventoryReplenishmentResponse response = inventoryAnalyticsService.getReplenishmentMetrics(companyId, warehouseId, storeId);
        return ResponseEntity.ok(ApiResponse.success("Inventory replenishment metrics retrieved successfully", response));
    }

    /**
     * Retrieves traceability metrics data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/traceability-metrics")
    @PreAuthorize("hasAuthority('INVENTORY_ANALYTICS_VIEW')")
    @Operation(summary = "Get inventory recall and product traceability metrics")
    public ResponseEntity<ApiResponse<InventoryTraceabilityResponse>> getTraceabilityMetrics(
            @RequestParam Long companyId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long storeId
    ) {
        InventoryTraceabilityResponse response = inventoryAnalyticsService.getTraceabilityMetrics(companyId, warehouseId, storeId);
        return ResponseEntity.ok(ApiResponse.success("Inventory traceability metrics retrieved successfully", response));
    }

    /**
     * Retrieves health data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/health")
    @PreAuthorize("hasAuthority('INVENTORY_ANALYTICS_VIEW')")
    @Operation(summary = "Get database refresh health status log")
    public ResponseEntity<ApiResponse<List<AnalyticsHealthResponse>>> getHealth() {
        List<AnalyticsHealthResponse> response = inventoryAnalyticsService.getHealth();
        return ResponseEntity.ok(ApiResponse.success("Database refresh health log retrieved successfully", response));
    }

    /**
     * Performs the refresh operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/refresh")
    @PreAuthorize("hasAuthority('INVENTORY_ANALYTICS_REFRESH')")
    @Operation(summary = "Manually trigger refresh of all inventory materialized views")
    public ResponseEntity<ApiResponse<Void>> refresh() {
        inventoryAnalyticsService.refreshAllViews();
        return ResponseEntity.ok(ApiResponse.success("Inventory analytics views refresh triggered successfully", null));
    }
}