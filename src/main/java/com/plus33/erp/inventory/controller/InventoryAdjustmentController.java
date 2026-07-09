/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.controller
 * File              : InventoryAdjustmentController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAdjustmentController
 * Related Service   : InventoryAdjustmentControllerService, InventoryAdjustmentControllerServiceImpl
 * Related Repository: InventoryAdjustmentControllerRepository
 * Related Entity    : InventoryAdjustmentController
 * Related DTO       : ApiResponse, InventoryAdjustmentRequest, InventoryAdjustmentResponse, InventoryAdjustmentSearchRequest, InventoryAdjustmentUpdateRequest
 * Related Mapper    : InventoryAdjustmentControllerMapper
 * Related DB Table  : inventory_adjustment_controllers
 * Related REST APIs : POST /api/v1/inventory-adjustments, PUT /api/v1/inventory-adjustments/{id}, GET /api/v1/inventory-adjustments/{id}, GET /api/v1/inventory-adjustments
 * Depends On        : Common Module
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Inventory Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/inventory-adjustments, PUT /api/v1/inventory-adjustments/{id}, GET /api/v1/inventory-adjustments/{id}, GET /api/v1/inventory-adjustments
 ******************************************************************************/
package com.plus33.erp.inventory.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.InventoryAdjustmentStatus;
import com.plus33.erp.inventory.entity.InventoryAdjustmentType;
import com.plus33.erp.inventory.service.InventoryAdjustmentService;
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
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryAdjustmentController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to InventoryAdjustmentService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> InventoryAdjustmentController.endpoint()
 *   --> InventoryAdjustmentService.method()
 *   --> InventoryAdjustmentRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/inventory-adjustments, PUT /api/v1/inventory-adjustments/{id}, GET /api/v1/inventory-adjustments/{id}, GET /api/v1/inventory-adjustments, POST /api/v1/inventory-adjustments/{id}/submit</p>
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/inventory-adjustments")
@Tag(name = "Inventory Adjustments", description = "REST APIs for managing physical stock adjustments and write-offs")
public class InventoryAdjustmentController {

    private final InventoryAdjustmentService inventoryAdjustmentService;

    public InventoryAdjustmentController(InventoryAdjustmentService inventoryAdjustmentService) {
        this.inventoryAdjustmentService = inventoryAdjustmentService;
    }

    /**
     * Creates a new adjustment and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('INVENTORY_ADJUSTMENT_CREATE')")
    @Operation(summary = "Create inventory adjustment", description = "Creates an adjustment in DRAFT status. Supports client_reference_id for idempotency.")
    public ResponseEntity<ApiResponse<InventoryAdjustmentResponse>> createAdjustment(
            @Valid @RequestBody InventoryAdjustmentRequest request
    ) {
        IdempotentCreateResult<InventoryAdjustmentResponse> result = inventoryAdjustmentService.createAdjustment(request);
        HttpStatus status = result.created() ? HttpStatus.CREATED : HttpStatus.OK;
        String msg = result.created() ? "Inventory adjustment created successfully" : "Inventory adjustment replayed successfully";
        return new ResponseEntity<>(ApiResponse.success(msg, result.data()), status);
    }

    /**
     * Updates an existing adjustment record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_ADJUSTMENT_UPDATE')")
    @Operation(summary = "Update inventory adjustment", description = "Updates an adjustment in DRAFT status.")
    public ResponseEntity<ApiResponse<InventoryAdjustmentResponse>> updateAdjustment(
            @PathVariable Long id,
            @Valid @RequestBody InventoryAdjustmentUpdateRequest request
    ) {
        InventoryAdjustmentResponse response = inventoryAdjustmentService.updateAdjustment(id, request);
        return ResponseEntity.ok(ApiResponse.success("Inventory adjustment updated successfully", response));
    }

    /**
     * Retrieves a single adjustment by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_ADJUSTMENT_VIEW')")
    @Operation(summary = "Get inventory adjustment by ID")
    public ResponseEntity<ApiResponse<InventoryAdjustmentResponse>> getAdjustmentById(@PathVariable Long id) {
        InventoryAdjustmentResponse response = inventoryAdjustmentService.getAdjustmentById(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory adjustment retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of adjustments records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('INVENTORY_ADJUSTMENT_VIEW')")
    @Operation(summary = "Search inventory adjustments")
    public ResponseEntity<ApiResponse<PageResponse<InventoryAdjustmentResponse>>> searchAdjustments(
            @RequestParam(required = false) InventoryAdjustmentStatus status,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) InventoryAdjustmentType adjustmentType,
            @RequestParam(required = false) String adjustmentNumber,
            @RequestParam(required = false) UUID clientReferenceId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate createdAtTo,
            @RequestParam(required = false) Long createdBy,
            @RequestParam(required = false) Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));

        InventoryAdjustmentSearchRequest searchRequest = InventoryAdjustmentSearchRequest.builder()
                .status(status)
                .companyId(companyId)
                .warehouseId(warehouseId)
                .storeId(storeId)
                .adjustmentType(adjustmentType)
                .adjustmentNumber(adjustmentNumber)
                .clientReferenceId(clientReferenceId)
                .createdAtFrom(createdAtFrom)
                .createdAtTo(createdAtTo)
                .createdBy(createdBy)
                .productId(productId)
                .build();

        PageResponse<InventoryAdjustmentResponse> response = inventoryAdjustmentService.searchAdjustments(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Inventory adjustments retrieved successfully", response));
    }

    /**
     * Submits the adjustment for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('INVENTORY_ADJUSTMENT_CREATE')")
    @Operation(summary = "Submit inventory adjustment")
    public ResponseEntity<ApiResponse<InventoryAdjustmentResponse>> submitAdjustment(@PathVariable Long id) {
        InventoryAdjustmentResponse response = inventoryAdjustmentService.submitAdjustment(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory adjustment submitted successfully", response));
    }

    /**
     * Approves the adjustment, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('INVENTORY_ADJUSTMENT_APPROVE')")
    @Operation(summary = "Approve inventory adjustment")
    public ResponseEntity<ApiResponse<InventoryAdjustmentResponse>> approveAdjustment(@PathVariable Long id) {
        InventoryAdjustmentResponse response = inventoryAdjustmentService.approveAdjustment(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory adjustment approved successfully", response));
    }

    /**
     * Posts adjustment entries to the General Ledger and updates financial balances.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/post")
    @PreAuthorize("hasAuthority('INVENTORY_ADJUSTMENT_POST')")
    @Operation(summary = "Post inventory adjustment")
    public ResponseEntity<ApiResponse<InventoryAdjustmentResponse>> postAdjustment(@PathVariable Long id) {
        InventoryAdjustmentResponse response = inventoryAdjustmentService.postAdjustment(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory adjustment posted successfully", response));
    }

    /**
     * Cancels the adjustment and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('INVENTORY_ADJUSTMENT_CANCEL')")
    @Operation(summary = "Cancel inventory adjustment")
    public ResponseEntity<ApiResponse<InventoryAdjustmentResponse>> cancelAdjustment(
            @PathVariable Long id,
            @RequestParam String reason
    ) {
        InventoryAdjustmentResponse response = inventoryAdjustmentService.cancelAdjustment(id, reason);
        return ResponseEntity.ok(ApiResponse.success("Inventory adjustment cancelled successfully", response));
    }
}