/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.controller
 * File              : SupplierInvoiceController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierInvoiceController
 * Related Service   : SupplierInvoiceControllerService, SupplierInvoiceControllerServiceImpl
 * Related Repository: SupplierInvoiceControllerRepository
 * Related Entity    : SupplierInvoiceController
 * Related DTO       : ApiResponse, PageRequest, PageResponse, searchRequest, SupplierInvoiceRequest
 * Related Mapper    : SupplierInvoiceControllerMapper
 * Related DB Table  : supplier_invoice_controllers
 * Related REST APIs : POST /api/v1/supplier-invoices, PUT /api/v1/supplier-invoices/{id}, GET /api/v1/supplier-invoices/{id}, GET /api/v1/supplier-invoices
 * Depends On        : Common Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Finance Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/supplier-invoices, PUT /api/v1/supplier-invoices/{id}, GET /api/v1/supplier-invoices/{id}, GET /api/v1/supplier-invoices
 ******************************************************************************/
package com.plus33.erp.finance.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.finance.dto.*;
import com.plus33.erp.finance.entity.SupplierInvoiceStatus;
import com.plus33.erp.finance.service.SupplierInvoiceService;
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
 * <p><b>Class  :</b> {@code SupplierInvoiceController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to SupplierInvoiceService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> SupplierInvoiceController.endpoint()
 *   --> SupplierInvoiceService.method()
 *   --> SupplierInvoiceRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/supplier-invoices, PUT /api/v1/supplier-invoices/{id}, GET /api/v1/supplier-invoices/{id}, GET /api/v1/supplier-invoices, POST /api/v1/supplier-invoices/{id}/approve</p>
 * <p><b>Module Deps      :</b> Common, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/supplier-invoices")
@Tag(name = "Supplier Invoice Management", description = "REST APIs for managing supplier invoices and financial entries")
public class SupplierInvoiceController {

    private final SupplierInvoiceService supplierInvoiceService;

    public SupplierInvoiceController(SupplierInvoiceService supplierInvoiceService) {
        this.supplierInvoiceService = supplierInvoiceService;
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
    @PreAuthorize("hasAuthority('SUPPLIER_INVOICE_CREATE')")
    @Operation(summary = "Create a new supplier invoice", description = "Creates a supplier invoice in DRAFT status.")
    public ResponseEntity<ApiResponse<SupplierInvoiceResponse>> createInvoice(
            @Valid @RequestBody SupplierInvoiceRequest request
    ) {
        SupplierInvoiceResponse response = supplierInvoiceService.createInvoice(request);
        return new ResponseEntity<>(ApiResponse.success("Supplier invoice created successfully", response), HttpStatus.CREATED);
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
    @PreAuthorize("hasAuthority('SUPPLIER_INVOICE_UPDATE')")
    @Operation(summary = "Update an existing supplier invoice", description = "Updates details and items of a DRAFT supplier invoice.")
    public ResponseEntity<ApiResponse<SupplierInvoiceResponse>> updateInvoice(
            @PathVariable Long id,
            @Valid @RequestBody SupplierInvoiceUpdateRequest request
    ) {
        SupplierInvoiceResponse response = supplierInvoiceService.updateInvoice(id, request);
        return ResponseEntity.ok(ApiResponse.success("Supplier invoice updated successfully", response));
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
    @PreAuthorize("hasAuthority('SUPPLIER_INVOICE_VIEW')")
    @Operation(summary = "Get supplier invoice by ID", description = "Retrieves details of a supplier invoice by ID.")
    public ResponseEntity<ApiResponse<SupplierInvoiceResponse>> getInvoiceById(@PathVariable Long id) {
        SupplierInvoiceResponse response = supplierInvoiceService.getInvoiceById(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier invoice retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of invoices records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('SUPPLIER_INVOICE_VIEW')")
    @Operation(summary = "Search supplier invoices", description = "Searches and filters supplier invoices with pagination.")
    public ResponseEntity<ApiResponse<PageResponse<SupplierInvoiceResponse>>> searchInvoices(
            @RequestParam(required = false) String invoiceNumber,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Long purchaseOrderId,
            @RequestParam(required = false) SupplierInvoiceStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate invoiceDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate invoiceDateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        SupplierInvoiceSearchRequest searchRequest = SupplierInvoiceSearchRequest.builder()
                .invoiceNumber(invoiceNumber)
                .companyId(companyId)
                .supplierId(supplierId)
                .purchaseOrderId(purchaseOrderId)
                .status(status)
                .invoiceDateFrom(invoiceDateFrom)
                .invoiceDateTo(invoiceDateTo)
                .build();
        PageResponse<SupplierInvoiceResponse> response = supplierInvoiceService.searchInvoices(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Supplier invoices retrieved successfully", response));
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
    @PreAuthorize("hasAuthority('SUPPLIER_INVOICE_APPROVE')")
    @Operation(summary = "Approve a supplier invoice", description = "Approves a draft supplier invoice, increments PO invoiced quantities, and generates journal entries.")
    public ResponseEntity<ApiResponse<SupplierInvoiceResponse>> approveInvoice(@PathVariable Long id) {
        SupplierInvoiceResponse response = supplierInvoiceService.approveInvoice(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier invoice approved successfully", response));
    }

    /**
     * Cancels the invoice and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('SUPPLIER_INVOICE_CANCEL')")
    @Operation(summary = "Cancel a supplier invoice", description = "Cancels an approved or draft supplier invoice, reverses PO quantities and posts reversing journal entries.")
    public ResponseEntity<ApiResponse<SupplierInvoiceResponse>> cancelInvoice(@PathVariable Long id) {
        SupplierInvoiceResponse response = supplierInvoiceService.cancelInvoice(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier invoice cancelled successfully", response));
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
    @PreAuthorize("hasAuthority('SUPPLIER_INVOICE_UPDATE')")
    @Operation(summary = "Submit a supplier invoice", description = "Submits a draft supplier invoice, progressing it to SUBMITTED status.")
    public ResponseEntity<ApiResponse<SupplierInvoiceResponse>> submitInvoice(@PathVariable Long id) {
        SupplierInvoiceResponse response = supplierInvoiceService.submitInvoice(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier invoice submitted successfully", response));
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
    @PreAuthorize("hasAuthority('VENDOR_BILL_VOID')")
    @Operation(summary = "Void a supplier invoice", description = "Voids a draft or submitted supplier invoice, progressing it to VOID status.")
    public ResponseEntity<ApiResponse<SupplierInvoiceResponse>> voidInvoice(@PathVariable Long id) {
        SupplierInvoiceResponse response = supplierInvoiceService.voidInvoice(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier invoice voided successfully", response));
    }
}