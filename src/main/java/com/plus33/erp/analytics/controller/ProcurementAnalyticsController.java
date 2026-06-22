package com.plus33.erp.analytics.controller;

import com.plus33.erp.analytics.dto.*;
import com.plus33.erp.analytics.service.ProcurementAnalyticsService;
import com.plus33.erp.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/analytics/procurement")
@Tag(name = "Procurement Analytics", description = "REST APIs for procurement KPIs and analytics dashboards")
public class ProcurementAnalyticsController {

    private final ProcurementAnalyticsService procurementAnalyticsService;

    public ProcurementAnalyticsController(ProcurementAnalyticsService procurementAnalyticsService) {
        this.procurementAnalyticsService = procurementAnalyticsService;
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    @Operation(summary = "Get procurement summary statistics")
    public ResponseEntity<ApiResponse<ProcurementSummaryResponse>> getSummary(
            @RequestParam Long companyId
    ) {
        ProcurementSummaryResponse response = procurementAnalyticsService.getSummary(companyId);
        return ResponseEntity.ok(ApiResponse.success("Procurement summary retrieved successfully", response));
    }

    @GetMapping("/suppliers")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    @Operation(summary = "Get supplier performance metrics")
    public ResponseEntity<ApiResponse<List<SupplierPerformanceResponse>>> getSuppliers(
            @RequestParam Long companyId
    ) {
        List<SupplierPerformanceResponse> response = procurementAnalyticsService.getSuppliers(companyId);
        return ResponseEntity.ok(ApiResponse.success("Supplier performance metrics retrieved successfully", response));
    }

    @GetMapping("/payables-aging")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    @Operation(summary = "Get payables aging buckets")
    public ResponseEntity<ApiResponse<List<PayablesAgingResponse>>> getPayablesAging(
            @RequestParam Long companyId
    ) {
        List<PayablesAgingResponse> response = procurementAnalyticsService.getPayablesAging(companyId);
        return ResponseEntity.ok(ApiResponse.success("Payables aging retrieved successfully", response));
    }

    @GetMapping("/purchase-orders")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    @Operation(summary = "Get purchase order fulfilment rates")
    public ResponseEntity<ApiResponse<List<PoFulfilmentResponse>>> getPurchaseOrders(
            @RequestParam Long companyId
    ) {
        List<PoFulfilmentResponse> response = procurementAnalyticsService.getPurchaseOrders(companyId);
        return ResponseEntity.ok(ApiResponse.success("Purchase order fulfilment rates retrieved successfully", response));
    }

    @GetMapping("/invoice-matching")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    @Operation(summary = "Get invoice matching exception details")
    public ResponseEntity<ApiResponse<List<InvoiceMatchingResponse>>> getInvoiceMatching(
            @RequestParam Long companyId
    ) {
        List<InvoiceMatchingResponse> response = procurementAnalyticsService.getInvoiceMatching(companyId);
        return ResponseEntity.ok(ApiResponse.success("Invoice matching exceptions retrieved successfully", response));
    }
}
