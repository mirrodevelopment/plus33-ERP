/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.controller
 * File              : PurchaseRequestController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseRequestController
 * Related Service   : PurchaseRequestControllerService, PurchaseRequestControllerServiceImpl
 * Related Repository: PurchaseRequestControllerRepository
 * Related Entity    : PurchaseRequestController
 * Related DTO       : ApiResponse, approvePurchaseRequest, cancelPurchaseRequest, createPurchaseRequest, PageRequest
 * Related Mapper    : PurchaseRequestControllerMapper
 * Related DB Table  : purchase_request_controllers
 * Related REST APIs : POST /api/v1/purchase-requests, GET /api/v1/purchase-requests/{id}, GET /api/v1/purchase-requests, PUT /api/v1/purchase-requests/{id}
 * Depends On        : Common Module
 * Used By           : Procurement Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Procurement Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/purchase-requests, GET /api/v1/purchase-requests/{id}, GET /api/v1/purchase-requests, PUT /api/v1/purchase-requests/{id}
 ******************************************************************************/
package com.plus33.erp.procurement.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.procurement.dto.*;
import com.plus33.erp.procurement.entity.PurchaseRequestStatus;
import com.plus33.erp.procurement.service.PurchaseRequestService;
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
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseRequestController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to PurchaseRequestService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> PurchaseRequestController.endpoint()
 *   --> PurchaseRequestService.method()
 *   --> PurchaseRequestRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/purchase-requests, GET /api/v1/purchase-requests/{id}, GET /api/v1/purchase-requests, PUT /api/v1/purchase-requests/{id}, POST /api/v1/purchase-requests/{id}/submit</p>
 * <p><b>Module Deps      :</b> Common, Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/purchase-requests")
@Tag(name = "Purchase Request Management", description = "REST APIs for managing purchase requests and approval workflow")
public class PurchaseRequestController {

    private final PurchaseRequestService purchaseRequestService;

    public PurchaseRequestController(PurchaseRequestService purchaseRequestService) {
        this.purchaseRequestService = purchaseRequestService;
    }

    /**
     * Creates a new purchase request and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_CREATE')")
    @Operation(summary = "Create a new purchase request", description = "Initiates a purchase request in DRAFT status.")
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> createPurchaseRequest(
            @Valid @RequestBody PurchaseRequestRequest request
    ) {
        PurchaseRequestResponse response = purchaseRequestService.createPurchaseRequest(request);
        return new ResponseEntity<>(ApiResponse.success("Purchase request created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single purchase request by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_VIEW')")
    @Operation(summary = "Get purchase request by ID", description = "Retrieves details of a purchase request by primary key.")
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> getPurchaseRequestById(@PathVariable Long id) {
        PurchaseRequestResponse response = purchaseRequestService.getPurchaseRequestById(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase request retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of purchase requests records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_VIEW')")
    @Operation(summary = "Search purchase requests", description = "Performs dynamic searches and pagination filters for purchase requests.")
    public ResponseEntity<ApiResponse<PageResponse<PurchaseRequestResponse>>> searchPurchaseRequests(
            @RequestParam(required = false) String requestNumber,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) PurchaseRequestStatus status,
            @RequestParam(required = false) Long requestedBy,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestDateTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requiredDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requiredDateTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        PurchaseRequestSearchRequest searchRequest = new PurchaseRequestSearchRequest(
                requestNumber, companyId, supplierId, warehouseId, storeId, status, requestedBy,
                requestDateFrom, requestDateTo, requiredDateFrom, requiredDateTo);
        PageResponse<PurchaseRequestResponse> response = purchaseRequestService.searchPurchaseRequests(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Purchase requests retrieved successfully", response));
    }

    /**
     * Updates an existing purchase request record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_UPDATE')")
    @Operation(summary = "Update purchase request details", description = "Modifies details of a purchase request while it is in DRAFT status.")
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> updatePurchaseRequest(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseRequestRequest request
    ) {
        PurchaseRequestResponse response = purchaseRequestService.updatePurchaseRequest(id, request);
        return ResponseEntity.ok(ApiResponse.success("Purchase request updated successfully", response));
    }

    /**
     * Submits the purchase request for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_SUBMIT')")
    @Operation(summary = "Submit purchase request", description = "Submits a purchase request in DRAFT status for approval.")
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> submitPurchaseRequest(@PathVariable Long id) {
        PurchaseRequestResponse response = purchaseRequestService.submitPurchaseRequest(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase request submitted successfully", response));
    }

    /**
     * Approves the purchase request, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_APPROVE')")
    @Operation(summary = "Approve purchase request", description = "Approves a submitted purchase request.")
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> approvePurchaseRequest(@PathVariable Long id) {
        PurchaseRequestResponse response = purchaseRequestService.approvePurchaseRequest(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase request approved successfully", response));
    }

    /**
     * Performs the rejectPurchaseRequest operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_REJECT')")
    @Operation(summary = "Reject purchase request", description = "Rejects a submitted purchase request with a reason.")
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> rejectPurchaseRequest(
            @PathVariable Long id,
            @Valid @RequestBody ReasonRequest reasonRequest
    ) {
        PurchaseRequestResponse response = purchaseRequestService.rejectPurchaseRequest(id, reasonRequest.reason());
        return ResponseEntity.ok(ApiResponse.success("Purchase request rejected successfully", response));
    }

    /**
     * Cancels the purchase request and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_CANCEL')")
    @Operation(summary = "Cancel purchase request", description = "Cancels a submitted purchase request with a reason.")
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> cancelPurchaseRequest(
            @PathVariable Long id,
            @Valid @RequestBody ReasonRequest reasonRequest
    ) {
        PurchaseRequestResponse response = purchaseRequestService.cancelPurchaseRequest(id, reasonRequest.reason());
        return ResponseEntity.ok(ApiResponse.success("Purchase request cancelled successfully", response));
    }

    /**
     * Converts between Entity and DTO representations (MapStruct).
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/convert")
    @PreAuthorize("hasAuthority('PURCHASE_REQUEST_CONVERT')")
    @Operation(summary = "Convert approved purchase request to PO", description = "Converts an approved purchase request to a Purchase Order draft.")
    public ResponseEntity<ApiResponse<PurchaseRequestResponse>> convertPurchaseRequestToPo(@PathVariable Long id) {
        PurchaseRequestResponse response = purchaseRequestService.convertPurchaseRequestToPo(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase request converted to PO successfully", response));
    }
}