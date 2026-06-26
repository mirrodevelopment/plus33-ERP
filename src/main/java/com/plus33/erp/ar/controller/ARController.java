package com.plus33.erp.ar.controller;

import com.plus33.erp.ar.dto.*;
import com.plus33.erp.ar.service.ARService;
import com.plus33.erp.ar.service.ARWriteOffService;
import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ar")
@Tag(name = "Accounts Receivable Management", description = "REST APIs for Accounts Receivable, aging, statements, and write-offs")
public class ARController {

    private final ARService arService;
    private final ARWriteOffService arWriteOffService;

    public ARController(ARService arService, ARWriteOffService arWriteOffService) {
        this.arService = arService;
        this.arWriteOffService = arWriteOffService;
    }

    @GetMapping("/aging")
    @PreAuthorize("hasAuthority('AR_VIEW')")
    @Operation(summary = "Get AR Aging Report", description = "Retrieves aging buckets from the mv_customer_aging materialized view.")
    public ResponseEntity<ApiResponse<List<ARAgingResponse>>> getAgingReport(
            @RequestParam Long companyId,
            @RequestParam(required = false) Long customerId
    ) {
        List<ARAgingResponse> response = arService.getAgingReport(companyId, customerId);
        return ResponseEntity.ok(ApiResponse.success("AR Aging Report retrieved successfully", response));
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('AR_VIEW')")
    @Operation(summary = "Get AR Summary / Dashboard", description = "Retrieves company-wide AR summary metrics.")
    public ResponseEntity<ApiResponse<ARSummaryResponse>> getARSummary(
            @RequestParam Long companyId
    ) {
        ARSummaryResponse response = arService.getARSummary(companyId);
        return ResponseEntity.ok(ApiResponse.success("AR Summary retrieved successfully", response));
    }

    @GetMapping("/customers/{customerId}/balance")
    @PreAuthorize("hasAuthority('AR_VIEW')")
    @Operation(summary = "Get Customer AR Balance", description = "Retrieves the current AR position for a single customer.")
    public ResponseEntity<ApiResponse<CustomerARBalanceResponse>> getCustomerARBalance(
            @PathVariable Long customerId,
            @RequestParam Long companyId
    ) {
        CustomerARBalanceResponse response = arService.getCustomerARBalance(customerId, companyId);
        return ResponseEntity.ok(ApiResponse.success("Customer AR Balance retrieved successfully", response));
    }

    @GetMapping("/customers/{customerId}/statement")
    @PreAuthorize("hasAuthority('AR_STATEMENT_VIEW')")
    @Operation(summary = "Get Customer AR Statement", description = "Builds a chronological ledger statement for a customer within a date range.")
    public ResponseEntity<ApiResponse<CustomerStatementResponse>> getCustomerStatement(
            @PathVariable Long customerId,
            @RequestParam Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to
    ) {
        CustomerStatementResponse response = arService.getCustomerStatement(customerId, companyId, from, to);
        return ResponseEntity.ok(ApiResponse.success("Customer AR Statement retrieved successfully", response));
    }

    @GetMapping("/overdue")
    @PreAuthorize("hasAuthority('AR_VIEW')")
    @Operation(summary = "Get Overdue Invoices", description = "Retrieves a paginated list of all overdue invoices.")
    public ResponseEntity<ApiResponse<PageResponse<AROverdueInvoiceResponse>>> getOverdueInvoices(
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
        PageResponse<AROverdueInvoiceResponse> response = arService.getOverdueInvoices(companyId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Overdue invoices retrieved successfully", response));
    }

    @PostMapping("/write-offs")
    @PreAuthorize("hasAuthority('AR_WRITE_OFF_CREATE')")
    @Operation(summary = "Record AR Write-off", description = "Records a bad-debt write-off, posts a GL journal entry, and updates invoice and customer balances.")
    public ResponseEntity<ApiResponse<ARWriteOffResponse>> createWriteOff(
            @Valid @RequestBody ARWriteOffRequest request
    ) {
        ARWriteOffResponse response = arWriteOffService.createWriteOff(request);
        return new ResponseEntity<>(ApiResponse.success("AR Write-off recorded successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/write-offs/{id}")
    @PreAuthorize("hasAuthority('AR_VIEW')")
    @Operation(summary = "Get AR Write-off by ID", description = "Retrieves detailed information of an AR write-off record.")
    public ResponseEntity<ApiResponse<ARWriteOffResponse>> getWriteOffById(
            @PathVariable Long id
    ) {
        ARWriteOffResponse response = arWriteOffService.getWriteOffById(id);
        return ResponseEntity.ok(ApiResponse.success("AR Write-off retrieved successfully", response));
    }

    @GetMapping("/write-offs")
    @PreAuthorize("hasAuthority('AR_VIEW')")
    @Operation(summary = "Search AR Write-offs", description = "Searches write-off records with optional customer and company filters.")
    public ResponseEntity<ApiResponse<PageResponse<ARWriteOffResponse>>> searchWriteOffs(
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id,desc") String sort
    ) {
        String[] sortParams = sort.split(",");
        String sortField = sortParams[0];
        Sort.Direction sortDir = sortParams.length > 1 && "asc".equalsIgnoreCase(sortParams[1])
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDir, sortField));
        PageResponse<ARWriteOffResponse> response = arWriteOffService.searchWriteOffs(companyId, customerId, pageable);
        return ResponseEntity.ok(ApiResponse.success("AR Write-offs retrieved successfully", response));
    }
}
