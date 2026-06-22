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

@RestController
@RequestMapping("/api/v1/analytics/inventory")
@Tag(name = "Inventory Analytics", description = "REST APIs for inventory dashboards, KPIs, and refresh monitoring")
public class InventoryAnalyticsController {

    private final InventoryAnalyticsService inventoryAnalyticsService;

    public InventoryAnalyticsController(InventoryAnalyticsService inventoryAnalyticsService) {
        this.inventoryAnalyticsService = inventoryAnalyticsService;
    }

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

    @GetMapping("/health")
    @PreAuthorize("hasAuthority('INVENTORY_ANALYTICS_VIEW')")
    @Operation(summary = "Get database refresh health status log")
    public ResponseEntity<ApiResponse<List<AnalyticsHealthResponse>>> getHealth() {
        List<AnalyticsHealthResponse> response = inventoryAnalyticsService.getHealth();
        return ResponseEntity.ok(ApiResponse.success("Database refresh health log retrieved successfully", response));
    }

    @PostMapping("/refresh")
    @PreAuthorize("hasAuthority('INVENTORY_ANALYTICS_REFRESH')")
    @Operation(summary = "Manually trigger refresh of all inventory materialized views")
    public ResponseEntity<ApiResponse<Void>> refresh() {
        inventoryAnalyticsService.refreshAllViews();
        return ResponseEntity.ok(ApiResponse.success("Inventory analytics views refresh triggered successfully", null));
    }
}
