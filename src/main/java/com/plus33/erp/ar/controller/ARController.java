/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ar Module
 * Package           : com.plus33.erp.ar.controller
 * File              : ARController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Ar Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ARController
 * Related Service   : ARControllerService, ARControllerServiceImpl
 * Related Repository: ARControllerRepository
 * Related Entity    : ARController
 * Related DTO       : ApiResponse, ARAgingResponse, AROverdueInvoiceResponse, ARSummaryResponse, ARWriteOffRequest
 * Related Mapper    : ARControllerMapper
 * Related DB Table  : a_r_controllers
 * Related REST APIs : GET /api/v1/ar/aging, GET /api/v1/ar/summary, GET /api/v1/ar/customers/{customerId}/balance, GET /api/v1/ar/customers/{customerId}/statement
 * Depends On        : Common Module
 * Used By           : Ar Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Ar Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: GET /api/v1/ar/aging, GET /api/v1/ar/summary, GET /api/v1/ar/customers/{customerId}/balance, GET /api/v1/ar/customers/{customerId}/statement
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Ar Module</b>
 *
 * <p><b>Class  :</b> {@code ARController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.ar.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to ARService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> ARController.endpoint()
 *   --> ARService.method()
 *   --> ARRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> GET /api/v1/ar/aging, GET /api/v1/ar/summary, GET /api/v1/ar/customers/{customerId}/balance, GET /api/v1/ar/customers/{customerId}/statement, GET /api/v1/ar/overdue</p>
 * <p><b>Module Deps      :</b> Ar, Common</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Retrieves aging report data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
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

    /**
     * Retrieves a r summary data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/summary")
    @PreAuthorize("hasAuthority('AR_VIEW')")
    @Operation(summary = "Get AR Summary / Dashboard", description = "Retrieves company-wide AR summary metrics.")
    public ResponseEntity<ApiResponse<ARSummaryResponse>> getARSummary(
            @RequestParam Long companyId
    ) {
        ARSummaryResponse response = arService.getARSummary(companyId);
        return ResponseEntity.ok(ApiResponse.success("AR Summary retrieved successfully", response));
    }

    /**
     * Retrieves customer a r balance data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
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

    /**
     * Retrieves customer statement data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
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

    /**
     * Retrieves overdue invoices data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
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

    /**
     * Creates a new write off and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/write-offs")
    @PreAuthorize("hasAuthority('AR_WRITE_OFF_CREATE')")
    @Operation(summary = "Record AR Write-off", description = "Records a bad-debt write-off, posts a GL journal entry, and updates invoice and customer balances.")
    public ResponseEntity<ApiResponse<ARWriteOffResponse>> createWriteOff(
            @Valid @RequestBody ARWriteOffRequest request
    ) {
        ARWriteOffResponse response = arWriteOffService.createWriteOff(request);
        return new ResponseEntity<>(ApiResponse.success("AR Write-off recorded successfully", response), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single write off by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/write-offs/{id}")
    @PreAuthorize("hasAuthority('AR_VIEW')")
    @Operation(summary = "Get AR Write-off by ID", description = "Retrieves detailed information of an AR write-off record.")
    public ResponseEntity<ApiResponse<ARWriteOffResponse>> getWriteOffById(
            @PathVariable Long id
    ) {
        ARWriteOffResponse response = arWriteOffService.getWriteOffById(id);
        return ResponseEntity.ok(ApiResponse.success("AR Write-off retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of write offs records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
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