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

@RestController
@RequestMapping("/api/v1/payment-runs")
@Tag(name = "Payment Run Management", description = "REST APIs for automated supplier payment runs and batch execution")
public class PaymentRunController {

    private final PaymentRunService paymentRunService;

    public PaymentRunController(PaymentRunService paymentRunService) {
        this.paymentRunService = paymentRunService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PAYMENT_RUN_CREATE')")
    @Operation(summary = "Create a draft payment run", description = "Initializes a new payment run in DRAFT status with configurable filters.")
    public ResponseEntity<ApiResponse<PaymentRunResponse>> createPaymentRun(
            @Valid @RequestBody PaymentRunRequest request
    ) {
        PaymentRunResponse response = paymentRunService.createPaymentRun(request);
        return new ResponseEntity<>(ApiResponse.success("Payment run created successfully", response), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/calculate")
    @PreAuthorize("hasAuthority('PAYMENT_RUN_CREATE')")
    @Operation(summary = "Calculate eligible invoices for payment run", description = "Automatically selects unpaid or partially paid supplier invoices matching the run filters, reserving them to prevent double payment.")
    public ResponseEntity<ApiResponse<PaymentRunResponse>> calculatePaymentRun(
            @PathVariable Long id
    ) {
        PaymentRunResponse response = paymentRunService.calculatePaymentRun(id);
        return ResponseEntity.ok(ApiResponse.success("Payment run calculated successfully", response));
    }

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

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('PAYMENT_RUN_APPROVE')")
    @Operation(summary = "Approve payment run", description = "Approves a calculated payment run, locking it against further changes.")
    public ResponseEntity<ApiResponse<PaymentRunResponse>> approvePaymentRun(
            @PathVariable Long id
    ) {
        PaymentRunResponse response = paymentRunService.approvePaymentRun(id);
        return ResponseEntity.ok(ApiResponse.success("Payment run approved successfully", response));
    }

    @PostMapping("/{id}/execute")
    @PreAuthorize("hasAuthority('PAYMENT_RUN_EXECUTE')")
    @Operation(summary = "Execute payment run", description = "Executes the payment run by grouping invoices by supplier, creating consolidated payments/allocations, and generating bank export files.")
    public ResponseEntity<ApiResponse<PaymentRunResponse>> executePaymentRun(
            @PathVariable Long id
    ) {
        PaymentRunResponse response = paymentRunService.executePaymentRun(id);
        return ResponseEntity.ok(ApiResponse.success("Payment run executed successfully", response));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('PAYMENT_RUN_CREATE')")
    @Operation(summary = "Cancel payment run", description = "Cancels a payment run, releasing all reserved supplier invoices.")
    public ResponseEntity<ApiResponse<PaymentRunResponse>> cancelPaymentRun(
            @PathVariable Long id
    ) {
        PaymentRunResponse response = paymentRunService.cancelPaymentRun(id);
        return ResponseEntity.ok(ApiResponse.success("Payment run cancelled successfully", response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PAYMENT_RUN_VIEW')")
    @Operation(summary = "Get payment run details by ID", description = "Retrieves payment run header, invoices, supplier results, and file storage metadata.")
    public ResponseEntity<ApiResponse<PaymentRunResponse>> getPaymentRunById(
            @PathVariable Long id
    ) {
        PaymentRunResponse response = paymentRunService.getPaymentRunById(id);
        return ResponseEntity.ok(ApiResponse.success("Payment run details retrieved successfully", response));
    }

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
