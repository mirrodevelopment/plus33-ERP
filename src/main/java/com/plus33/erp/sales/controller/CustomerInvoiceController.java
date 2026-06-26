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

@RestController
@RequestMapping("/api/v1/customer-invoices")
@Tag(name = "Customer Invoice Management", description = "REST APIs for managing customer invoices and financial postings")
public class CustomerInvoiceController {

    private final CustomerInvoiceService customerInvoiceService;

    public CustomerInvoiceController(CustomerInvoiceService customerInvoiceService) {
        this.customerInvoiceService = customerInvoiceService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER_INVOICE_CREATE')")
    @Operation(summary = "Create a new customer invoice", description = "Creates a customer invoice in DRAFT status.")
    public ResponseEntity<ApiResponse<CustomerInvoiceResponse>> createInvoice(
            @Valid @RequestBody CustomerInvoiceRequest request
    ) {
        CustomerInvoiceResponse response = customerInvoiceService.createInvoice(request);
        return new ResponseEntity<>(ApiResponse.success("Customer invoice created successfully", response), HttpStatus.CREATED);
    }

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

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_INVOICE_VIEW')")
    @Operation(summary = "Get customer invoice by ID", description = "Retrieves details of a customer invoice by ID.")
    public ResponseEntity<ApiResponse<CustomerInvoiceResponse>> getInvoiceById(@PathVariable Long id) {
        CustomerInvoiceResponse response = customerInvoiceService.getInvoiceById(id);
        return ResponseEntity.ok(ApiResponse.success("Customer invoice retrieved successfully", response));
    }

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

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('CUSTOMER_INVOICE_SUBMIT')")
    @Operation(summary = "Submit a customer invoice", description = "Submits a draft customer invoice for approval.")
    public ResponseEntity<ApiResponse<CustomerInvoiceResponse>> submitInvoice(@PathVariable Long id) {
        CustomerInvoiceResponse response = customerInvoiceService.submitInvoice(id);
        return ResponseEntity.ok(ApiResponse.success("Customer invoice submitted successfully", response));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('CUSTOMER_INVOICE_APPROVE')")
    @Operation(summary = "Approve a customer invoice", description = "Approves a submitted customer invoice, posts a journal entry, and increments invoiced quantities on sales order items.")
    public ResponseEntity<ApiResponse<CustomerInvoiceResponse>> approveInvoice(@PathVariable Long id) {
        CustomerInvoiceResponse response = customerInvoiceService.approveInvoice(id);
        return ResponseEntity.ok(ApiResponse.success("Customer invoice approved successfully", response));
    }

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

    @PostMapping("/{id}/void")
    @PreAuthorize("hasAuthority('CUSTOMER_INVOICE_VOID')")
    @Operation(summary = "Void a customer invoice", description = "Voids a draft or submitted customer invoice before approval.")
    public ResponseEntity<ApiResponse<CustomerInvoiceResponse>> voidInvoice(@PathVariable Long id) {
        CustomerInvoiceResponse response = customerInvoiceService.voidInvoice(id);
        return ResponseEntity.ok(ApiResponse.success("Customer invoice voided successfully", response));
    }
}
