/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.controller
 * File              : InventoryTransferController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTransferController
 * Related Service   : InventoryTransferControllerService, InventoryTransferControllerServiceImpl
 * Related Repository: InventoryTransferControllerRepository
 * Related Entity    : InventoryTransferController
 * Related DTO       : ApiResponse, InventoryTransferRequest, InventoryTransferResponse, InventoryTransferSearchRequest, InventoryTransferUpdateRequest
 * Related Mapper    : InventoryTransferControllerMapper
 * Related DB Table  : inventory_transfer_controllers
 * Related REST APIs : POST /api/v1/inventory-transfers, PUT /api/v1/inventory-transfers/{id}, GET /api/v1/inventory-transfers/{id}, GET /api/v1/inventory-transfers
 * Depends On        : Common Module
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Inventory Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/inventory-transfers, PUT /api/v1/inventory-transfers/{id}, GET /api/v1/inventory-transfers/{id}, GET /api/v1/inventory-transfers
 ******************************************************************************/
package com.plus33.erp.inventory.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.entity.InventoryTransferStatus;
import com.plus33.erp.inventory.service.InventoryTransferService;
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
 * <p><b>Class  :</b> {@code InventoryTransferController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to InventoryTransferService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> InventoryTransferController.endpoint()
 *   --> InventoryTransferService.method()
 *   --> InventoryTransferRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/inventory-transfers, PUT /api/v1/inventory-transfers/{id}, GET /api/v1/inventory-transfers/{id}, GET /api/v1/inventory-transfers, POST /api/v1/inventory-transfers/{id}/submit</p>
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/inventory-transfers")
@Tag(name = "Inventory Transfers", description = "REST APIs for managing internal inventory transfers between warehouses and stores")
public class InventoryTransferController {

    private final InventoryTransferService inventoryTransferService;

    public InventoryTransferController(InventoryTransferService inventoryTransferService) {
        this.inventoryTransferService = inventoryTransferService;
    }

    /**
     * Creates a new transfer and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('INVENTORY_TRANSFER_CREATE')")
    @Operation(summary = "Create inventory transfer", description = "Creates a transfer in DRAFT status. Supports client_reference_id for idempotency.")
    public ResponseEntity<ApiResponse<InventoryTransferResponse>> createTransfer(
            @Valid @RequestBody InventoryTransferRequest request
    ) {
        IdempotentCreateResult<InventoryTransferResponse> result = inventoryTransferService.createTransfer(request);
        HttpStatus status = result.created() ? HttpStatus.CREATED : HttpStatus.OK;
        String msg = result.created() ? "Inventory transfer created successfully" : "Inventory transfer replayed successfully";
        return new ResponseEntity<>(ApiResponse.success(msg, result.data()), status);
    }

    /**
     * Updates an existing transfer record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_TRANSFER_UPDATE')")
    @Operation(summary = "Update inventory transfer", description = "Updates a transfer in DRAFT status.")
    public ResponseEntity<ApiResponse<InventoryTransferResponse>> updateTransfer(
            @PathVariable Long id,
            @Valid @RequestBody InventoryTransferUpdateRequest request
    ) {
        InventoryTransferResponse response = inventoryTransferService.updateTransfer(id, request);
        return ResponseEntity.ok(ApiResponse.success("Inventory transfer updated successfully", response));
    }

    /**
     * Retrieves a single transfer by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_TRANSFER_VIEW')")
    @Operation(summary = "Get inventory transfer by ID")
    public ResponseEntity<ApiResponse<InventoryTransferResponse>> getTransferById(@PathVariable Long id) {
        InventoryTransferResponse response = inventoryTransferService.getTransferById(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory transfer retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of transfers records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('INVENTORY_TRANSFER_VIEW')")
    @Operation(summary = "Search inventory transfers")
    public ResponseEntity<ApiResponse<PageResponse<InventoryTransferResponse>>> searchTransfers(
            @RequestParam(required = false) InventoryTransferStatus status,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long sourceWarehouseId,
            @RequestParam(required = false) Long sourceStoreId,
            @RequestParam(required = false) Long destWarehouseId,
            @RequestParam(required = false) Long destStoreId,
            @RequestParam(required = false) String transferNumber,
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

        InventoryTransferSearchRequest searchRequest = InventoryTransferSearchRequest.builder()
                .status(status)
                .companyId(companyId)
                .sourceWarehouseId(sourceWarehouseId)
                .sourceStoreId(sourceStoreId)
                .destWarehouseId(destWarehouseId)
                .destStoreId(destStoreId)
                .transferNumber(transferNumber)
                .clientReferenceId(clientReferenceId)
                .createdAtFrom(createdAtFrom)
                .createdAtTo(createdAtTo)
                .createdBy(createdBy)
                .productId(productId)
                .build();

        PageResponse<InventoryTransferResponse> response = inventoryTransferService.searchTransfers(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Inventory transfers retrieved successfully", response));
    }

    /**
     * Submits the transfer for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('INVENTORY_TRANSFER_CREATE')")
    @Operation(summary = "Submit inventory transfer")
    public ResponseEntity<ApiResponse<InventoryTransferResponse>> submitTransfer(@PathVariable Long id) {
        InventoryTransferResponse response = inventoryTransferService.submitTransfer(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory transfer submitted successfully", response));
    }

    /**
     * Approves the transfer, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('INVENTORY_TRANSFER_APPROVE')")
    @Operation(summary = "Approve inventory transfer")
    public ResponseEntity<ApiResponse<InventoryTransferResponse>> approveTransfer(@PathVariable Long id) {
        InventoryTransferResponse response = inventoryTransferService.approveTransfer(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory transfer approved successfully", response));
    }

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/dispatch")
    @PreAuthorize("hasAuthority('INVENTORY_TRANSFER_DISPATCH')")
    @Operation(summary = "Dispatch inventory transfer")
    public ResponseEntity<ApiResponse<InventoryTransferResponse>> dispatchTransfer(@PathVariable Long id) {
        InventoryTransferResponse response = inventoryTransferService.dispatchTransfer(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory transfer dispatched successfully", response));
    }

    /**
     * Performs the receiveTransfer operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/receive")
    @PreAuthorize("hasAuthority('INVENTORY_TRANSFER_RECEIVE')")
    @Operation(summary = "Receive inventory transfer")
    public ResponseEntity<ApiResponse<InventoryTransferResponse>> receiveTransfer(@PathVariable Long id) {
        InventoryTransferResponse response = inventoryTransferService.receiveTransfer(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory transfer received successfully", response));
    }

    /**
     * Cancels the transfer and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('INVENTORY_TRANSFER_CANCEL')")
    @Operation(summary = "Cancel inventory transfer")
    public ResponseEntity<ApiResponse<InventoryTransferResponse>> cancelTransfer(
            @PathVariable Long id,
            @RequestParam String reason
    ) {
        InventoryTransferResponse response = inventoryTransferService.cancelTransfer(id, reason);
        return ResponseEntity.ok(ApiResponse.success("Inventory transfer cancelled successfully", response));
    }
}