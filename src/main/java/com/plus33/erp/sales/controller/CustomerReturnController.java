package com.plus33.erp.sales.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.sales.dto.*;
import com.plus33.erp.sales.entity.CustomerReturnStatus;
import com.plus33.erp.sales.service.CustomerReturnService;
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
@RequestMapping("/api/v1/customer-returns")
@Tag(name = "Customer Return Management", description = "REST APIs for managing customer returns, inspections, and restocking")
public class CustomerReturnController {

    private final CustomerReturnService customerReturnService;

    public CustomerReturnController(CustomerReturnService customerReturnService) {
        this.customerReturnService = customerReturnService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER_RETURN_CREATE')")
    @Operation(summary = "Create a new customer return request", description = "Creates a customer return request in RETURN_REQUESTED status.")
    public ResponseEntity<ApiResponse<CustomerReturnResponse>> createReturn(
            @Valid @RequestBody CustomerReturnRequest request
    ) {
        CustomerReturnResponse response = customerReturnService.createReturn(request);
        return new ResponseEntity<>(ApiResponse.success("Customer return request created successfully", response), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_RETURN_UPDATE')")
    @Operation(summary = "Update an existing customer return request", description = "Updates details and items of a RETURN_REQUESTED customer return request.")
    public ResponseEntity<ApiResponse<CustomerReturnResponse>> updateReturn(
            @PathVariable Long id,
            @Valid @RequestBody CustomerReturnRequest request
    ) {
        CustomerReturnResponse response = customerReturnService.updateReturn(id, request);
        return ResponseEntity.ok(ApiResponse.success("Customer return request updated successfully", response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_RETURN_VIEW')")
    @Operation(summary = "Get a customer return by ID")
    public ResponseEntity<ApiResponse<CustomerReturnResponse>> getReturnById(@PathVariable Long id) {
        CustomerReturnResponse response = customerReturnService.getReturnById(id);
        return ResponseEntity.ok(ApiResponse.success("Customer return retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('CUSTOMER_RETURN_VIEW')")
    @Operation(summary = "Search customer returns with filtering and pagination")
    public ResponseEntity<ApiResponse<PageResponse<CustomerReturnResponse>>> searchReturns(
            @RequestParam(required = false) String returnNumber,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long salesOrderId,
            @RequestParam(required = false) Long customerInvoiceId,
            @RequestParam(required = false) CustomerReturnStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDateTo,
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

        CustomerReturnSearchRequest searchRequest = CustomerReturnSearchRequest.builder()
                .returnNumber(returnNumber)
                .companyId(companyId)
                .customerId(customerId)
                .salesOrderId(salesOrderId)
                .customerInvoiceId(customerInvoiceId)
                .status(status)
                .returnDateFrom(returnDateFrom)
                .returnDateTo(returnDateTo)
                .build();

        PageResponse<CustomerReturnResponse> response = customerReturnService.searchReturns(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Customer returns retrieved successfully", response));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('CUSTOMER_RETURN_APPROVE')")
    @Operation(summary = "Approve a customer return request")
    public ResponseEntity<ApiResponse<CustomerReturnResponse>> approveReturn(
            @PathVariable Long id,
            @Valid @RequestBody ReturnApprovalRequest request
    ) {
        CustomerReturnResponse response = customerReturnService.approveReturn(id, request);
        return ResponseEntity.ok(ApiResponse.success("Customer return request approved successfully", response));
    }

    @PostMapping("/{id}/receive")
    @PreAuthorize("hasAuthority('CUSTOMER_RETURN_RECEIVE')")
    @Operation(summary = "Mark a customer return as received")
    public ResponseEntity<ApiResponse<CustomerReturnResponse>> receiveReturn(@PathVariable Long id) {
        CustomerReturnResponse response = customerReturnService.receiveReturn(id);
        return ResponseEntity.ok(ApiResponse.success("Customer return received successfully", response));
    }

    @PostMapping("/{id}/inspect")
    @PreAuthorize("hasAuthority('CUSTOMER_RETURN_INSPECT')")
    @Operation(summary = "Record inspection results for returned items")
    public ResponseEntity<ApiResponse<CustomerReturnResponse>> inspectReturn(
            @PathVariable Long id,
            @Valid @RequestBody InspectionRequest request
    ) {
        CustomerReturnResponse response = customerReturnService.inspectReturn(id, request);
        return ResponseEntity.ok(ApiResponse.success("Customer return inspection completed successfully", response));
    }

    @PostMapping("/{id}/close")
    @PreAuthorize("hasAuthority('CUSTOMER_RETURN_CLOSE')")
    @Operation(summary = "Close a customer return, trigger restocking/scrapping, and post a credit note")
    public ResponseEntity<ApiResponse<CustomerReturnResponse>> closeReturn(
            @PathVariable Long id,
            @Valid @RequestBody ReturnCloseRequest request
    ) {
        CustomerReturnResponse response = customerReturnService.closeReturn(id, request);
        return ResponseEntity.ok(ApiResponse.success("Customer return closed and credit note posted successfully", response));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('CUSTOMER_RETURN_CANCEL')")
    @Operation(summary = "Cancel a customer return request")
    public ResponseEntity<ApiResponse<CustomerReturnResponse>> cancelReturn(
            @PathVariable Long id,
            @RequestParam String reason
    ) {
        CustomerReturnResponse response = customerReturnService.cancelReturn(id, reason);
        return ResponseEntity.ok(ApiResponse.success("Customer return request cancelled successfully", response));
    }
}
