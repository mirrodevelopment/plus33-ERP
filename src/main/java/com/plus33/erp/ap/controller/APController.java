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

@RestController
@RequestMapping("/api/v1/ap")
@Tag(name = "Accounts Payable Management", description = "REST APIs for Accounts Payable reporting, aging, and supplier statements")
public class APController {

    private final APService apService;

    public APController(APService apService) {
        this.apService = apService;
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('AP_VIEW')")
    @Operation(summary = "Get AP Dashboard Metrics", description = "Retrieves company-wide Accounts Payable dashboard summary and KPIs.")
    public ResponseEntity<ApiResponse<APDashboardResponse>> getAPDashboard(
            @RequestParam Long companyId
    ) {
        APDashboardResponse response = apService.getAPDashboard(companyId);
        return ResponseEntity.ok(ApiResponse.success("AP Dashboard retrieved successfully", response));
    }

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
