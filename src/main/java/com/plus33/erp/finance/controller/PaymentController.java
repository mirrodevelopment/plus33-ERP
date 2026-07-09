/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.controller
 * File              : PaymentController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentController
 * Related Service   : PaymentControllerService, PaymentControllerServiceImpl
 * Related Repository: PaymentControllerRepository
 * Related Entity    : PaymentController
 * Related DTO       : ApiResponse, PageRequest, PageResponse, PaymentCancelRequest, PaymentRequest
 * Related Mapper    : PaymentControllerMapper
 * Related DB Table  : payment_controllers
 * Related REST APIs : POST /api/v1/payments, GET /api/v1/payments/{id}, GET /api/v1/payments, POST /api/v1/payments/{id}/cancel
 * Depends On        : Common Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Finance Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/payments, GET /api/v1/payments/{id}, GET /api/v1/payments, POST /api/v1/payments/{id}/cancel
 ******************************************************************************/
package com.plus33.erp.finance.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.finance.dto.*;
import com.plus33.erp.finance.entity.PaymentStatus;
import com.plus33.erp.finance.service.PaymentService;
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

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to PaymentService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> PaymentController.endpoint()
 *   --> PaymentService.method()
 *   --> PaymentRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/payments, GET /api/v1/payments/{id}, GET /api/v1/payments, POST /api/v1/payments/{id}/cancel</p>
 * <p><b>Module Deps      :</b> Common, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payment Management", description = "REST APIs for managing supplier payments and accounts payable offsets")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    /**
     * Creates a new payment and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PAYMENT_CREATE')")
    @Operation(summary = "Record and allocate supplier payment", description = "Records a payment transaction, allocates it to invoices, and posts the journal entry.")
    public ResponseEntity<ApiResponse<PaymentResponse>> createPayment(
            @Valid @RequestBody PaymentRequest request
    ) {
        PaymentResponse response = paymentService.createPayment(request);
        return new ResponseEntity<>(ApiResponse.success("Supplier payment recorded and allocated successfully", response), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single payment by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PAYMENT_VIEW')")
    @Operation(summary = "Get payment details by ID", description = "Retrieves payment header, metadata, and allocations.")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPaymentById(@PathVariable Long id) {
        PaymentResponse response = paymentService.getPaymentById(id);
        return ResponseEntity.ok(ApiResponse.success("Payment details retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of payments records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('PAYMENT_VIEW')")
    @Operation(summary = "Search payments with filtering", description = "Searches and filters payments with pagination.")
    public ResponseEntity<ApiResponse<PageResponse<PaymentResponse>>> searchPayments(
            @RequestParam(required = false) String paymentNumber,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) PaymentStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paymentDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate paymentDateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        PaymentSearchRequest searchRequest = PaymentSearchRequest.builder()
                .paymentNumber(paymentNumber)
                .companyId(companyId)
                .supplierId(supplierId)
                .paymentMethod(paymentMethod)
                .status(status)
                .paymentDateFrom(paymentDateFrom)
                .paymentDateTo(paymentDateTo)
                .build();
        PageResponse<PaymentResponse> response = paymentService.searchPayments(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Payments retrieved successfully", response));
    }

    /**
     * Cancels the payment and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('PAYMENT_CANCEL')")
    @Operation(summary = "Cancel a supplier payment", description = "Cancels a payment, deallocates it from invoices, and posts a reversing journal entry.")
    public ResponseEntity<ApiResponse<PaymentResponse>> cancelPayment(
            @PathVariable Long id,
            @Valid @RequestBody PaymentCancelRequest request
    ) {
        PaymentResponse response = paymentService.cancelPayment(id, request);
        return ResponseEntity.ok(ApiResponse.success("Payment cancelled successfully", response));
    }
}