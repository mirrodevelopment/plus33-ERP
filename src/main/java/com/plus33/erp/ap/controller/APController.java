/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ap Module
 * Package           : com.plus33.erp.ap.controller
 * File              : APController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Ap Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: APController
 * Related Service   : APControllerService, APControllerServiceImpl
 * Related Repository: APControllerRepository
 * Related Entity    : APController
 * Related DTO       : APAgingResponse, APAnalyticsResponse, APDashboardResponse, ApiResponse, APOverdueBillResponse
 * Related Mapper    : APControllerMapper
 * Related DB Table  : a_p_controllers
 * Related REST APIs : GET /api/v1/ap/dashboard, GET /api/v1/ap/aging, GET /api/v1/ap/suppliers/{supplierId}/balance, GET /api/v1/ap/suppliers/{supplierId}/statement
 * Depends On        : Common Module
 * Used By           : Ap Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Ap Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: GET /api/v1/ap/dashboard, GET /api/v1/ap/aging, GET /api/v1/ap/suppliers/{supplierId}/balance, GET /api/v1/ap/suppliers/{supplierId}/statement
 ******************************************************************************/
package com.plus33.erp.ap.controller;

import com.plus33.erp.ap.dto.*;
import com.plus33.erp.ap.service.APService;
import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Ap Module</b>
 *
 * <p><b>Class  :</b> {@code APController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.ap.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to APService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> APController.endpoint()
 *   --> APService.method()
 *   --> APRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> GET /api/v1/ap/dashboard, GET /api/v1/ap/aging, GET /api/v1/ap/suppliers/{supplierId}/balance, GET /api/v1/ap/suppliers/{supplierId}/statement, GET /api/v1/ap/overdue</p>
 * <p><b>Module Deps      :</b> Ap, Common</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/ap")
@Tag(name = "Accounts Payable Management", description = "REST APIs for Accounts Payable reporting, aging, and supplier statements")
public class APController {

    private final APService apService;

    public APController(APService apService) {
        this.apService = apService;
    }

    /**
     * Retrieves a p dashboard data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('AP_VIEW')")
    @Operation(summary = "Get AP Dashboard Metrics", description = "Retrieves company-wide Accounts Payable dashboard summary and KPIs.")
    public ResponseEntity<ApiResponse<APDashboardResponse>> getAPDashboard(
            @RequestParam Long companyId
    ) {
        APDashboardResponse response = apService.getAPDashboard(companyId);
        return ResponseEntity.ok(ApiResponse.success("AP Dashboard retrieved successfully", response));
    }

    /**
     * Retrieves aging report data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/aging")
    @PreAuthorize("hasAuthority('AP_VIEW')")
    @Operation(summary = "Get AP Aging Report", description = "Retrieves aging buckets from outstanding supplier invoices, supporting configurable intervals.")
    public ResponseEntity<ApiResponse<List<APAgingResponse>>> getAgingReport(
            @RequestParam Long companyId,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) List<Integer> intervals
    ) {
        List<APAgingResponse> response = apService.getAgingReport(companyId, supplierId, intervals);
        return ResponseEntity.ok(ApiResponse.success("AP Aging Report retrieved successfully", response));
    }

    /**
     * Retrieves supplier a p balance data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/suppliers/{supplierId}/balance")
    @PreAuthorize("hasAuthority('AP_VIEW')")
    @Operation(summary = "Get Supplier AP Balance", description = "Retrieves the current AP position for a single supplier.")
    public ResponseEntity<ApiResponse<SupplierAPBalanceResponse>> getSupplierAPBalance(
            @PathVariable Long supplierId,
            @RequestParam Long companyId
    ) {
        SupplierAPBalanceResponse response = apService.getSupplierAPBalance(supplierId, companyId);
        return ResponseEntity.ok(ApiResponse.success("Supplier AP Balance retrieved successfully", response));
    }

    /**
     * Retrieves supplier statement data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/suppliers/{supplierId}/statement")
    @PreAuthorize("hasAuthority('AP_STATEMENT_VIEW')")
    @Operation(summary = "Get Supplier Statement", description = "Builds a chronological statement/ledger for a supplier within a date range.")
    public ResponseEntity<ApiResponse<SupplierStatementResponse>> getSupplierStatement(
            @PathVariable Long supplierId,
            @RequestParam Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        SupplierStatementResponse response = apService.getSupplierStatement(supplierId, companyId, from, to);
        return ResponseEntity.ok(ApiResponse.success("Supplier statement retrieved successfully", response));
    }

    /**
     * Retrieves overdue bills data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/overdue")
    @PreAuthorize("hasAuthority('AP_VIEW')")
    @Operation(summary = "Get Overdue Bills", description = "Retrieves a paginated list of all overdue supplier invoices.")
    public ResponseEntity<ApiResponse<PageResponse<APOverdueBillResponse>>> getOverdueBills(
            @RequestParam Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dueDate,asc") String sort
    ) {
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction sortDir = sortParams.length > 1 && "desc".equalsIgnoreCase(sortParams[1])
                ? Sort.Direction.DESC
                : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortField));
        PageResponse<APOverdueBillResponse> response = apService.getOverdueBills(companyId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Overdue bills retrieved successfully", response));
    }

    /**
     * Retrieves a p analytics data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/analytics")
    @PreAuthorize("hasAuthority('AP_VIEW')")
    @Operation(summary = "Get AP Analytics", description = "Retrieves detailed AP analytics, payment trends, concentration, and cash forecast.")
    public ResponseEntity<ApiResponse<APAnalyticsResponse>> getAPAnalytics(
            @RequestParam Long companyId
    ) {
        APAnalyticsResponse response = apService.getAPAnalytics(companyId);
        return ResponseEntity.ok(ApiResponse.success("AP Analytics retrieved successfully", response));
    }
}