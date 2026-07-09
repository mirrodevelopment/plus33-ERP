/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.controller
 * File              : ProcurementAnalyticsController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcurementAnalyticsController
 * Related Service   : ProcurementAnalyticsControllerService, ProcurementAnalyticsControllerServiceImpl
 * Related Repository: ProcurementAnalyticsControllerRepository
 * Related Entity    : ProcurementAnalyticsController
 * Related DTO       : ApiResponse, InvoiceMatchingResponse, PayablesAgingResponse, PoFulfilmentResponse, ProcurementSummaryResponse
 * Related Mapper    : ProcurementAnalyticsControllerMapper
 * Related DB Table  : procurement_analytics_controllers
 * Related REST APIs : GET /api/v1/analytics/procurement/summary, GET /api/v1/analytics/procurement/suppliers, GET /api/v1/analytics/procurement/payables-aging, GET /api/v1/analytics/procurement/purchase-orders
 * Depends On        : Common Module
 * Used By           : Analytics Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Analytics Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: GET /api/v1/analytics/procurement/summary, GET /api/v1/analytics/procurement/suppliers, GET /api/v1/analytics/procurement/payables-aging, GET /api/v1/analytics/procurement/purchase-orders
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Analytics Module</b>
 *
 * <p><b>Class  :</b> {@code ProcurementAnalyticsController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.analytics.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to ProcurementAnalyticsService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> ProcurementAnalyticsController.endpoint()
 *   --> ProcurementAnalyticsService.method()
 *   --> ProcurementAnalyticsRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> GET /api/v1/analytics/procurement/summary, GET /api/v1/analytics/procurement/suppliers, GET /api/v1/analytics/procurement/payables-aging, GET /api/v1/analytics/procurement/purchase-orders, GET /api/v1/analytics/procurement/invoice-matching</p>
 * <p><b>Module Deps      :</b> Analytics, Common</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/analytics/procurement")
@Tag(name = "Procurement Analytics", description = "REST APIs for procurement KPIs and analytics dashboards")
public class ProcurementAnalyticsController {

    private final ProcurementAnalyticsService procurementAnalyticsService;

    public ProcurementAnalyticsController(ProcurementAnalyticsService procurementAnalyticsService) {
        this.procurementAnalyticsService = procurementAnalyticsService;
    }

    /**
     * Retrieves summary data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    @Operation(summary = "Get procurement summary statistics")
    public ResponseEntity<ApiResponse<ProcurementSummaryResponse>> getSummary(
            @RequestParam Long companyId
    ) {
        ProcurementSummaryResponse response = procurementAnalyticsService.getSummary(companyId);
        return ResponseEntity.ok(ApiResponse.success("Procurement summary retrieved successfully", response));
    }

    /**
     * Retrieves suppliers data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/suppliers")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    @Operation(summary = "Get supplier performance metrics")
    public ResponseEntity<ApiResponse<List<SupplierPerformanceResponse>>> getSuppliers(
            @RequestParam Long companyId
    ) {
        List<SupplierPerformanceResponse> response = procurementAnalyticsService.getSuppliers(companyId);
        return ResponseEntity.ok(ApiResponse.success("Supplier performance metrics retrieved successfully", response));
    }

    /**
     * Retrieves payables aging data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/payables-aging")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    @Operation(summary = "Get payables aging buckets")
    public ResponseEntity<ApiResponse<List<PayablesAgingResponse>>> getPayablesAging(
            @RequestParam Long companyId
    ) {
        List<PayablesAgingResponse> response = procurementAnalyticsService.getPayablesAging(companyId);
        return ResponseEntity.ok(ApiResponse.success("Payables aging retrieved successfully", response));
    }

    /**
     * Retrieves purchase orders data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/purchase-orders")
    @PreAuthorize("hasAuthority('ANALYTICS_VIEW')")
    @Operation(summary = "Get purchase order fulfilment rates")
    public ResponseEntity<ApiResponse<List<PoFulfilmentResponse>>> getPurchaseOrders(
            @RequestParam Long companyId
    ) {
        List<PoFulfilmentResponse> response = procurementAnalyticsService.getPurchaseOrders(companyId);
        return ResponseEntity.ok(ApiResponse.success("Purchase order fulfilment rates retrieved successfully", response));
    }

    /**
     * Retrieves invoice matching data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
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