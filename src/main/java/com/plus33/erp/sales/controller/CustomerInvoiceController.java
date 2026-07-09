/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.controller
 * File              : CustomerInvoiceController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerInvoiceController
 * Related Service   : CustomerInvoiceControllerService, CustomerInvoiceControllerServiceImpl
 * Related Repository: CustomerInvoiceControllerRepository
 * Related Entity    : CustomerInvoiceController
 * Related DTO       : ApiResponse, CustomerInvoiceRequest, CustomerInvoiceResponse, CustomerInvoiceSearchRequest, CustomerInvoiceUpdateRequest
 * Related Mapper    : CustomerInvoiceControllerMapper
 * Related DB Table  : customer_invoice_controllers
 * Related REST APIs : POST /api/v1/customer-invoices, PUT /api/v1/customer-invoices/{id}, GET /api/v1/customer-invoices/{id}, GET /api/v1/customer-invoices
 * Depends On        : Common Module
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Sales Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/customer-invoices, PUT /api/v1/customer-invoices/{id}, GET /api/v1/customer-invoices/{id}, GET /api/v1/customer-invoices
 ******************************************************************************/
package com.plus33.erp.sales.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.sales.dto.*;
import com.plus33.erp.sales.entity.CustomerInvoiceStatus;
import com.plus33.erp.sales.service.CustomerInvoiceService;
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
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerInvoiceController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to CustomerInvoiceService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> CustomerInvoiceController.endpoint()
 *   --> CustomerInvoiceService.method()
 *   --> CustomerInvoiceRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/customer-invoices, PUT /api/v1/customer-invoices/{id}, GET /api/v1/customer-invoices/{id}, GET /api/v1/customer-invoices, POST /api/v1/customer-invoices/{id}/submit</p>
 * <p><b>Module Deps      :</b> Common, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/customer-invoices")
@Tag(name = "Customer Invoice Management", description = "REST APIs for managing customer invoices and financial postings")
public class CustomerInvoiceController {

    private final CustomerInvoiceService customerInvoiceService;

    public CustomerInvoiceController(CustomerInvoiceService customerInvoiceService) {
        this.customerInvoiceService = customerInvoiceService;
    }

    /**
     * Creates a new invoice and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER_INVOICE_CREATE')")
    @Operation(summary = "Create a new customer invoice", description = "Creates a customer invoice in DRAFT status.")
    public ResponseEntity<ApiResponse<CustomerInvoiceResponse>> createInvoice(
            @Valid @RequestBody CustomerInvoiceRequest request
    ) {
        CustomerInvoiceResponse response = customerInvoiceService.createInvoice(request);
        return new ResponseEntity<>(ApiResponse.success("Customer invoice created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Updates an existing invoice record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_INVOICE_UPDATE')")
    @Operation(summary = "Update an existing customer invoice", description = "Updates details and items of a DRAFT customer invoice.")
    public ResponseEntity<ApiResponse<CustomerInvoiceResponse>> updateInvoice(
            @PathVariable Long id,
            @Valid @RequestBody CustomerInvoiceUpdateRequest request
    ) {
        CustomerInvoiceResponse response = customerInvoiceService.updateInvoice(id, request);
        return ResponseEntity.ok(ApiResponse.success("Customer invoice updated successfully", response));
    }

    /**
     * Retrieves a single invoice by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_INVOICE_VIEW')")
    @Operation(summary = "Get customer invoice by ID", description = "Retrieves details of a customer invoice by ID.")
    public ResponseEntity<ApiResponse<CustomerInvoiceResponse>> getInvoiceById(@PathVariable Long id) {
        CustomerInvoiceResponse response = customerInvoiceService.getInvoiceById(id);
        return ResponseEntity.ok(ApiResponse.success("Customer invoice retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of invoices records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('CUSTOMER_INVOICE_VIEW')")
    @Operation(summary = "Search customer invoices", description = "Searches and filters customer invoices with pagination.")
    public ResponseEntity<ApiResponse<PageResponse<CustomerInvoiceResponse>>> searchInvoices(
            @RequestParam(required = false) String invoiceNumber,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long salesOrderId,
            @RequestParam(required = false) CustomerInvoiceStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate invoiceDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate invoiceDateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        CustomerInvoiceSearchRequest searchRequest = CustomerInvoiceSearchRequest.builder()
                .invoiceNumber(invoiceNumber)
                .companyId(companyId)
                .customerId(customerId)
                .salesOrderId(salesOrderId)
                .status(status)
                .invoiceDateFrom(invoiceDateFrom)
                .invoiceDateTo(invoiceDateTo)
                .build();
        PageResponse<CustomerInvoiceResponse> response = customerInvoiceService.searchInvoices(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Customer invoices retrieved successfully", response));
    }

    /**
     * Submits the invoice for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('CUSTOMER_INVOICE_SUBMIT')")
    @Operation(summary = "Submit a customer invoice", description = "Submits a draft customer invoice for approval.")
    public ResponseEntity<ApiResponse<CustomerInvoiceResponse>> submitInvoice(@PathVariable Long id) {
        CustomerInvoiceResponse response = customerInvoiceService.submitInvoice(id);
        return ResponseEntity.ok(ApiResponse.success("Customer invoice submitted successfully", response));
    }

    /**
     * Approves the invoice, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('CUSTOMER_INVOICE_APPROVE')")
    @Operation(summary = "Approve a customer invoice", description = "Approves a submitted customer invoice, posts a journal entry, and increments invoiced quantities on sales order items.")
    public ResponseEntity<ApiResponse<CustomerInvoiceResponse>> approveInvoice(@PathVariable Long id) {
        CustomerInvoiceResponse response = customerInvoiceService.approveInvoice(id);
        return ResponseEntity.ok(ApiResponse.success("Customer invoice approved successfully", response));
    }

    /**
     * Cancels the invoice and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('CUSTOMER_INVOICE_CANCEL')")
    @Operation(summary = "Cancel a customer invoice", description = "Cancels an approved customer invoice, reverses sales order quantities and posts a reversing journal entry.")
    public ResponseEntity<ApiResponse<CustomerInvoiceResponse>> cancelInvoice(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "Cancelled by user") String reason
    ) {
        CustomerInvoiceResponse response = customerInvoiceService.cancelInvoice(id, reason);
        return ResponseEntity.ok(ApiResponse.success("Customer invoice cancelled successfully", response));
    }

    /**
     * Permanently voids the invoice. This action cannot be undone.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/void")
    @PreAuthorize("hasAuthority('CUSTOMER_INVOICE_VOID')")
    @Operation(summary = "Void a customer invoice", description = "Voids a draft or submitted customer invoice before approval.")
    public ResponseEntity<ApiResponse<CustomerInvoiceResponse>> voidInvoice(@PathVariable Long id) {
        CustomerInvoiceResponse response = customerInvoiceService.voidInvoice(id);
        return ResponseEntity.ok(ApiResponse.success("Customer invoice voided successfully", response));
    }
}