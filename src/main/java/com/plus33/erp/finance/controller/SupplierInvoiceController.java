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

@RestController
@RequestMapping("/api/v1/supplier-invoices")
@Tag(name = "Supplier Invoice Management", description = "REST APIs for managing supplier invoices and financial entries")
public class SupplierInvoiceController {

    private final SupplierInvoiceService supplierInvoiceService;

    public SupplierInvoiceController(SupplierInvoiceService supplierInvoiceService) {
        this.supplierInvoiceService = supplierInvoiceService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SUPPLIER_INVOICE_CREATE')")
    @Operation(summary = "Create a new supplier invoice", description = "Creates a supplier invoice in DRAFT status.")
    public ResponseEntity<ApiResponse<SupplierInvoiceResponse>> createInvoice(
            @Valid @RequestBody SupplierInvoiceRequest request
    ) {
        SupplierInvoiceResponse response = supplierInvoiceService.createInvoice(request);
        return new ResponseEntity<>(ApiResponse.success("Supplier invoice created successfully", response), HttpStatus.CREATED);
    }

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

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER_INVOICE_VIEW')")
    @Operation(summary = "Get supplier invoice by ID", description = "Retrieves details of a supplier invoice by ID.")
    public ResponseEntity<ApiResponse<SupplierInvoiceResponse>> getInvoiceById(@PathVariable Long id) {
        SupplierInvoiceResponse response = supplierInvoiceService.getInvoiceById(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier invoice retrieved successfully", response));
    }

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

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('SUPPLIER_INVOICE_APPROVE')")
    @Operation(summary = "Approve a supplier invoice", description = "Approves a draft supplier invoice, increments PO invoiced quantities, and generates journal entries.")
    public ResponseEntity<ApiResponse<SupplierInvoiceResponse>> approveInvoice(@PathVariable Long id) {
        SupplierInvoiceResponse response = supplierInvoiceService.approveInvoice(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier invoice approved successfully", response));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('SUPPLIER_INVOICE_CANCEL')")
    @Operation(summary = "Cancel a supplier invoice", description = "Cancels an approved or draft supplier invoice, reverses PO quantities and posts reversing journal entries.")
    public ResponseEntity<ApiResponse<SupplierInvoiceResponse>> cancelInvoice(@PathVariable Long id) {
        SupplierInvoiceResponse response = supplierInvoiceService.cancelInvoice(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier invoice cancelled successfully", response));
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('SUPPLIER_INVOICE_UPDATE')")
    @Operation(summary = "Submit a supplier invoice", description = "Submits a draft supplier invoice, progressing it to SUBMITTED status.")
    public ResponseEntity<ApiResponse<SupplierInvoiceResponse>> submitInvoice(@PathVariable Long id) {
        SupplierInvoiceResponse response = supplierInvoiceService.submitInvoice(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier invoice submitted successfully", response));
    }

    @PostMapping("/{id}/void")
    @PreAuthorize("hasAuthority('VENDOR_BILL_VOID')")
    @Operation(summary = "Void a supplier invoice", description = "Voids a draft or submitted supplier invoice, progressing it to VOID status.")
    public ResponseEntity<ApiResponse<SupplierInvoiceResponse>> voidInvoice(@PathVariable Long id) {
        SupplierInvoiceResponse response = supplierInvoiceService.voidInvoice(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier invoice voided successfully", response));
    }
}
