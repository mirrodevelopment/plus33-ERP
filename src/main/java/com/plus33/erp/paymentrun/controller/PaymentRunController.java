/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.controller
 * File              : PaymentRunController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Paymentrun Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentRunController
 * Related Service   : PaymentRunControllerService, PaymentRunControllerServiceImpl
 * Related Repository: PaymentRunControllerRepository
 * Related Entity    : PaymentRunController
 * Related DTO       : ApiResponse, PaymentRunDashboardResponse, PaymentRunInvoiceRequest, PaymentRunRequest, PaymentRunResponse
 * Related Mapper    : PaymentRunControllerMapper
 * Related DB Table  : payment_run_controllers
 * Related REST APIs : POST /api/v1/payment-runs, POST /api/v1/payment-runs/{id}/calculate, PUT /api/v1/payment-runs/{id}/invoices, POST /api/v1/payment-runs/{id}/approve
 * Depends On        : Common Module
 * Used By           : Paymentrun Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Paymentrun Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/payment-runs, POST /api/v1/payment-runs/{id}/calculate, PUT /api/v1/payment-runs/{id}/invoices, POST /api/v1/payment-runs/{id}/approve
 ******************************************************************************/
package com.plus33.erp.paymentrun.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.paymentrun.dto.*;
import com.plus33.erp.paymentrun.service.PaymentRunService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Paymentrun Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentRunController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.paymentrun.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to PaymentRunService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> PaymentRunController.endpoint()
 *   --> PaymentRunService.method()
 *   --> PaymentRunRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/payment-runs, POST /api/v1/payment-runs/{id}/calculate, PUT /api/v1/payment-runs/{id}/invoices, POST /api/v1/payment-runs/{id}/approve, POST /api/v1/payment-runs/{id}/execute</p>
 * <p><b>Module Deps      :</b> Common, Paymentrun</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/payment-runs")
@Tag(name = "Payment Run Management", description = "REST APIs for automated supplier payment runs and batch execution")
public class PaymentRunController {

    private final PaymentRunService paymentRunService;

    public PaymentRunController(PaymentRunService paymentRunService) {
        this.paymentRunService = paymentRunService;
    }

    /**
     * Creates a new payment run and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PAYMENT_RUN_CREATE')")
    @Operation(summary = "Create a draft payment run", description = "Initializes a new payment run in DRAFT status with configurable filters.")
    public ResponseEntity<ApiResponse<PaymentRunResponse>> createPaymentRun(
            @Valid @RequestBody PaymentRunRequest request
    ) {
        PaymentRunResponse response = paymentRunService.createPaymentRun(request);
        return new ResponseEntity<>(ApiResponse.success("Payment run created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Calculates payment run totals including subtotal, tax, discounts, and net amount.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/calculate")
    @PreAuthorize("hasAuthority('PAYMENT_RUN_CREATE')")
    @Operation(summary = "Calculate eligible invoices for payment run", description = "Automatically selects unpaid or partially paid supplier invoices matching the run filters, reserving them to prevent double payment.")
    public ResponseEntity<ApiResponse<PaymentRunResponse>> calculatePaymentRun(
            @PathVariable Long id
    ) {
        PaymentRunResponse response = paymentRunService.calculatePaymentRun(id);
        return ResponseEntity.ok(ApiResponse.success("Payment run calculated successfully", response));
    }

    /**
     * Updates an existing payment run invoices record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}/invoices")
    @PreAuthorize("hasAuthority('PAYMENT_RUN_CREATE')")
    @Operation(summary = "Update payment run invoices", description = "Allows adjusting payment amounts (supporting partial payments) or excluding invoices (releasing their lock).")
    public ResponseEntity<ApiResponse<PaymentRunResponse>> updatePaymentRunInvoices(
            @PathVariable Long id,
            @Valid @RequestBody List<PaymentRunInvoiceRequest> requests
    ) {
        PaymentRunResponse response = paymentRunService.updatePaymentRunInvoices(id, requests);
        return ResponseEntity.ok(ApiResponse.success("Payment run invoices updated successfully", response));
    }

    /**
     * Approves the payment run, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('PAYMENT_RUN_APPROVE')")
    @Operation(summary = "Approve payment run", description = "Approves a calculated payment run, locking it against further changes.")
    public ResponseEntity<ApiResponse<PaymentRunResponse>> approvePaymentRun(
            @PathVariable Long id
    ) {
        PaymentRunResponse response = paymentRunService.approvePaymentRun(id);
        return ResponseEntity.ok(ApiResponse.success("Payment run approved successfully", response));
    }

    /**
     * Performs the executePaymentRun operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/execute")
    @PreAuthorize("hasAuthority('PAYMENT_RUN_EXECUTE')")
    @Operation(summary = "Execute payment run", description = "Executes the payment run by grouping invoices by supplier, creating consolidated payments/allocations, and generating bank export files.")
    public ResponseEntity<ApiResponse<PaymentRunResponse>> executePaymentRun(
            @PathVariable Long id
    ) {
        PaymentRunResponse response = paymentRunService.executePaymentRun(id);
        return ResponseEntity.ok(ApiResponse.success("Payment run executed successfully", response));
    }

    /**
     * Cancels the payment run and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('PAYMENT_RUN_CREATE')")
    @Operation(summary = "Cancel payment run", description = "Cancels a payment run, releasing all reserved supplier invoices.")
    public ResponseEntity<ApiResponse<PaymentRunResponse>> cancelPaymentRun(
            @PathVariable Long id
    ) {
        PaymentRunResponse response = paymentRunService.cancelPaymentRun(id);
        return ResponseEntity.ok(ApiResponse.success("Payment run cancelled successfully", response));
    }

    /**
     * Retrieves a single payment run by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PAYMENT_RUN_VIEW')")
    @Operation(summary = "Get payment run details by ID", description = "Retrieves payment run header, invoices, supplier results, and file storage metadata.")
    public ResponseEntity<ApiResponse<PaymentRunResponse>> getPaymentRunById(
            @PathVariable Long id
    ) {
        PaymentRunResponse response = paymentRunService.getPaymentRunById(id);
        return ResponseEntity.ok(ApiResponse.success("Payment run details retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of payment runs records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     */
    @GetMapping
    @PreAuthorize("hasAuthority('PAYMENT_RUN_VIEW')")
    @Operation(summary = "Search payment runs", description = "Retrieves a list of payment runs matching company and status filters.")
    public ResponseEntity<ApiResponse<List<PaymentRunResponse>>> searchPaymentRuns(
            @RequestParam Long companyId,
            @RequestParam(required = false) String status
    ) {
        List<PaymentRunResponse> response = paymentRunService.searchPaymentRuns(companyId, status);
        return ResponseEntity.ok(ApiResponse.success("Payment runs retrieved successfully", response));
    }

    /**
     * Retrieves payment run dashboard data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/dashboard")
    @PreAuthorize("hasAuthority('PAYMENT_RUN_VIEW')")
    @Operation(summary = "Get payment run dashboard KPIs", description = "Retrieves basic financial KPIs along with rich operational statistics and averages for the dashboard.")
    public ResponseEntity<ApiResponse<PaymentRunDashboardResponse>> getPaymentRunDashboard(
            @RequestParam Long companyId
    ) {
        PaymentRunDashboardResponse response = paymentRunService.getPaymentRunDashboard(companyId);
        return ResponseEntity.ok(ApiResponse.success("Payment run dashboard retrieved successfully", response));
    }
}