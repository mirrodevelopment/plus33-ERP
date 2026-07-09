/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.controller
 * File              : SalesOrderController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesOrderController
 * Related Service   : SalesOrderControllerService, SalesOrderControllerServiceImpl
 * Related Repository: SalesOrderControllerRepository
 * Related Entity    : SalesOrderController
 * Related DTO       : ApiResponse, PageRequest, PageResponse, reasonRequest, SalesOrderCancelRequest
 * Related Mapper    : SalesOrderControllerMapper
 * Related DB Table  : sales_order_controllers
 * Related REST APIs : POST /api/v1/sales-orders, PUT /api/v1/sales-orders/{id}, GET /api/v1/sales-orders/{id}, GET /api/v1/sales-orders
 * Depends On        : Common Module
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Sales Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/sales-orders, PUT /api/v1/sales-orders/{id}, GET /api/v1/sales-orders/{id}, GET /api/v1/sales-orders
 ******************************************************************************/
package com.plus33.erp.sales.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.sales.dto.SalesOrderCancelRequest;
import com.plus33.erp.sales.dto.SalesOrderRequest;
import com.plus33.erp.sales.dto.SalesOrderResponse;
import com.plus33.erp.sales.dto.SalesOrderSearchRequest;
import com.plus33.erp.sales.entity.SalesOrderStatus;
import com.plus33.erp.sales.service.SalesOrderService;
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
 * <p><b>Class  :</b> {@code SalesOrderController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to SalesOrderService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> SalesOrderController.endpoint()
 *   --> SalesOrderService.method()
 *   --> SalesOrderRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/sales-orders, PUT /api/v1/sales-orders/{id}, GET /api/v1/sales-orders/{id}, GET /api/v1/sales-orders, POST /api/v1/sales-orders/{id}/submit</p>
 * <p><b>Module Deps      :</b> Common, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/sales-orders")
@Tag(name = "Sales Order Management", description = "REST APIs for managing sales orders and workflows")
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    public SalesOrderController(SalesOrderService salesOrderService) {
        this.salesOrderService = salesOrderService;
    }

    /**
     * Creates a new sales order and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('SALES_ORDER_CREATE')")
    @Operation(summary = "Create a new sales order", description = "Initializes a sales order in DRAFT status. Enforces customer and company validation, snapshots pricing, terms, and billing/shipping addresses. Idempotent check applied on clientReferenceId.")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> createSalesOrder(@Valid @RequestBody SalesOrderRequest request) {
        SalesOrderResponse response = salesOrderService.createSalesOrder(request);
        return new ResponseEntity<>(ApiResponse.success("Sales order created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Updates an existing sales order record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SALES_ORDER_UPDATE')")
    @Operation(summary = "Update draft sales order", description = "Modifies items and delivery dates for an order that is in DRAFT status.")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> updateSalesOrder(
            @PathVariable Long id,
            @Valid @RequestBody SalesOrderRequest request
    ) {
        SalesOrderResponse response = salesOrderService.updateSalesOrder(id, request);
        return ResponseEntity.ok(ApiResponse.success("Sales order updated successfully", response));
    }

    /**
     * Retrieves a single sales order by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SALES_ORDER_VIEW')")
    @Operation(summary = "Get sales order by ID", description = "Retrieves sales order details and its line items by ID.")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> getSalesOrderById(@PathVariable Long id) {
        SalesOrderResponse response = salesOrderService.getSalesOrderById(id);
        return ResponseEntity.ok(ApiResponse.success("Sales order retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of sales orders records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('SALES_ORDER_VIEW')")
    @Operation(summary = "Search sales orders", description = "Filter, search, and paginate sales orders dynamically.")
    public ResponseEntity<ApiResponse<PageResponse<SalesOrderResponse>>> searchSalesOrders(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) SalesOrderStatus status,
            @RequestParam(required = false) String customerType,
            @RequestParam(required = false) Boolean creditOverride,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestedDeliveryDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestedDeliveryDateTo,
            @RequestParam(required = false) Long createdBy,
            @RequestParam(required = false) Long approvedBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        SalesOrderSearchRequest searchRequest = new SalesOrderSearchRequest(
                query, companyId, customerId, status, customerType, creditOverride,
                requestedDeliveryDateFrom, requestedDeliveryDateTo, createdBy, approvedBy
        );
        PageResponse<SalesOrderResponse> response = salesOrderService.searchSalesOrders(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Sales orders retrieved successfully", response));
    }

    /**
     * Submits the sales order for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('SALES_ORDER_SUBMIT')")
    @Operation(summary = "Submit sales order", description = "Transitions sales order from DRAFT to SUBMITTED status.")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> submitSalesOrder(@PathVariable Long id) {
        SalesOrderResponse response = salesOrderService.submitSalesOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Sales order submitted successfully", response));
    }

    /**
     * Approves the sales order, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('SALES_ORDER_APPROVE')")
    @Operation(summary = "Approve sales order", description = "Transitions sales order from SUBMITTED to APPROVED status. Runs credit checks, updating the customer's outstanding balance.")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> approveSalesOrder(@PathVariable Long id) {
        SalesOrderResponse response = salesOrderService.approveSalesOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Sales order approved successfully", response));
    }

    /**
     * Cancels the sales order and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('SALES_ORDER_CANCEL')")
    @Operation(summary = "Cancel sales order", description = "Cancels a sales order in DRAFT, SUBMITTED, or APPROVED status. Reverts customer outstanding balance increment if approved.")
    public ResponseEntity<ApiResponse<SalesOrderResponse>> cancelSalesOrder(
            @PathVariable Long id,
            @Valid @RequestBody SalesOrderCancelRequest reasonRequest
    ) {
        SalesOrderResponse response = salesOrderService.cancelSalesOrder(id, reasonRequest.reason());
        return ResponseEntity.ok(ApiResponse.success("Sales order cancelled successfully", response));
    }
}