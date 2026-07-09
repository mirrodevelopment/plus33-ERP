/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.controller
 * File              : CustomerReturnController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerReturnController
 * Related Service   : CustomerReturnControllerService, CustomerReturnControllerServiceImpl
 * Related Repository: CustomerReturnControllerRepository
 * Related Entity    : CustomerReturnController
 * Related DTO       : ApiResponse, CustomerReturnRequest, CustomerReturnResponse, CustomerReturnSearchRequest, InspectionRequest
 * Related Mapper    : CustomerReturnControllerMapper
 * Related DB Table  : customer_return_controllers
 * Related REST APIs : POST /api/v1/customer-returns, PUT /api/v1/customer-returns/{id}, GET /api/v1/customer-returns/{id}, GET /api/v1/customer-returns
 * Depends On        : Common Module
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Sales Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/customer-returns, PUT /api/v1/customer-returns/{id}, GET /api/v1/customer-returns/{id}, GET /api/v1/customer-returns
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerReturnController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to CustomerReturnService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> CustomerReturnController.endpoint()
 *   --> CustomerReturnService.method()
 *   --> CustomerReturnRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/customer-returns, PUT /api/v1/customer-returns/{id}, GET /api/v1/customer-returns/{id}, GET /api/v1/customer-returns, POST /api/v1/customer-returns/{id}/approve</p>
 * <p><b>Module Deps      :</b> Common, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/customer-returns")
@Tag(name = "Customer Return Management", description = "REST APIs for managing customer returns, inspections, and restocking")
public class CustomerReturnController {

    private final CustomerReturnService customerReturnService;

    public CustomerReturnController(CustomerReturnService customerReturnService) {
        this.customerReturnService = customerReturnService;
    }

    /**
     * Creates a new return and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('CUSTOMER_RETURN_CREATE')")
    @Operation(summary = "Create a new customer return request", description = "Creates a customer return request in RETURN_REQUESTED status.")
    public ResponseEntity<ApiResponse<CustomerReturnResponse>> createReturn(
            @Valid @RequestBody CustomerReturnRequest request
    ) {
        CustomerReturnResponse response = customerReturnService.createReturn(request);
        return new ResponseEntity<>(ApiResponse.success("Customer return request created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Updates an existing return record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Retrieves a single return by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('CUSTOMER_RETURN_VIEW')")
    @Operation(summary = "Get a customer return by ID")
    public ResponseEntity<ApiResponse<CustomerReturnResponse>> getReturnById(@PathVariable Long id) {
        CustomerReturnResponse response = customerReturnService.getReturnById(id);
        return ResponseEntity.ok(ApiResponse.success("Customer return retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of returns records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
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

    /**
     * Approves the return, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Performs the receiveReturn operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/receive")
    @PreAuthorize("hasAuthority('CUSTOMER_RETURN_RECEIVE')")
    @Operation(summary = "Mark a customer return as received")
    public ResponseEntity<ApiResponse<CustomerReturnResponse>> receiveReturn(@PathVariable Long id) {
        CustomerReturnResponse response = customerReturnService.receiveReturn(id);
        return ResponseEntity.ok(ApiResponse.success("Customer return received successfully", response));
    }

    /**
     * Performs the inspectReturn operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
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

    /**
     * Completes the return workflow and finalizes the record status.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
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

    /**
     * Cancels the return and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
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