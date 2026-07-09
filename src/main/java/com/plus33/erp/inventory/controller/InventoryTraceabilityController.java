/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.controller
 * File              : InventoryTraceabilityController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTraceabilityController
 * Related Service   : InventoryTraceabilityControllerService, InventoryTraceabilityControllerServiceImpl
 * Related Repository: InventoryTraceabilityControllerRepository
 * Related Entity    : InventoryTraceabilityController
 * Related DTO       : ApiResponse, InventoryLotRequest, InventoryLotResponse, InventoryRecallRequest, InventoryRecallResponse
 * Related Mapper    : InventoryTraceabilityControllerMapper
 * Related DB Table  : inventory_traceability_controllers
 * Related REST APIs : POST /api/v1/inventory-lots, GET /api/v1/inventory-lots, GET /api/v1/inventory-lots/{id}, GET /api/v1/inventory-serials
 * Depends On        : Common Module
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Inventory Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/inventory-lots, GET /api/v1/inventory-lots, GET /api/v1/inventory-lots/{id}, GET /api/v1/inventory-serials
 ******************************************************************************/
package com.plus33.erp.inventory.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.service.InventoryTraceabilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryTraceabilityController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to InventoryTraceabilityService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> InventoryTraceabilityController.endpoint()
 *   --> InventoryTraceabilityService.method()
 *   --> InventoryTraceabilityRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/inventory-lots, GET /api/v1/inventory-lots, GET /api/v1/inventory-lots/{id}, GET /api/v1/inventory-serials, GET /api/v1/inventory-trace/{productId}</p>
 * <p><b>Module Deps      :</b> Common, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Inventory Traceability", description = "REST APIs for managing inventory lots, serials, recalls, and traces")
public class InventoryTraceabilityController {

    private final InventoryTraceabilityService service;

    public InventoryTraceabilityController(InventoryTraceabilityService service) {
        this.service = service;
    }

    /**
     * Creates a new lot and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/inventory-lots")
    @PreAuthorize("hasAuthority('INVENTORY_LOT_CREATE')")
    @Operation(summary = "Create a new inventory lot", description = "Defines a new product batch/lot with expiry tracking")
    public ResponseEntity<ApiResponse<InventoryLotResponse>> createLot(
            @Valid @RequestBody InventoryLotRequest request
    ) {
        InventoryLotResponse response = service.createLot(request);
        return new ResponseEntity<>(ApiResponse.success("Inventory lot created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Retrieves a paginated list of all lots records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/inventory-lots")
    @PreAuthorize("hasAuthority('INVENTORY_LOT_VIEW')")
    @Operation(summary = "List all inventory lots")
    public ResponseEntity<ApiResponse<List<InventoryLotResponse>>> getAllLots() {
        List<InventoryLotResponse> response = service.getAllLots();
        return ResponseEntity.ok(ApiResponse.success("Inventory lots retrieved successfully", response));
    }

    /**
     * Retrieves a single lot by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/inventory-lots/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_LOT_VIEW')")
    @Operation(summary = "Get single inventory lot by ID")
    public ResponseEntity<ApiResponse<InventoryLotResponse>> getLotById(@PathVariable Long id) {
        InventoryLotResponse response = service.getLotById(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory lot retrieved successfully", response));
    }

    /**
     * Retrieves a paginated list of all serials records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/inventory-serials")
    @PreAuthorize("hasAuthority('INVENTORY_SERIAL_VIEW')")
    @Operation(summary = "List all serialized items")
    public ResponseEntity<ApiResponse<List<InventorySerialResponse>>> getAllSerials() {
        List<InventorySerialResponse> response = service.getAllSerials();
        return ResponseEntity.ok(ApiResponse.success("Inventory serials retrieved successfully", response));
    }

    /**
     * Retrieves product trace data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param productId the productId input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/inventory-trace/{productId}")
    @PreAuthorize("hasAuthority('INVENTORY_TRACE_VIEW')")
    @Operation(summary = "Trace history for product", description = "Returns end-to-end genealogy trace ledger for a product")
    public ResponseEntity<ApiResponse<List<InventoryTraceEventResponse>>> getProductTrace(@PathVariable Long productId) {
        List<InventoryTraceEventResponse> response = service.getProductTrace(productId);
        return ResponseEntity.ok(ApiResponse.success("Product trace history retrieved successfully", response));
    }

    /**
     * Retrieves lot trace data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param lotNumber the lotNumber input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/inventory-trace/lot/{lotNumber}")
    @PreAuthorize("hasAuthority('INVENTORY_TRACE_VIEW')")
    @Operation(summary = "Trace history by lot number")
    public ResponseEntity<ApiResponse<List<InventoryTraceEventResponse>>> getLotTrace(@PathVariable String lotNumber) {
        List<InventoryTraceEventResponse> response = service.getLotTrace(lotNumber);
        return ResponseEntity.ok(ApiResponse.success("Lot trace history retrieved successfully", response));
    }

    /**
     * Retrieves serial trace data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param serialNumber the serialNumber input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/inventory-trace/serial/{serialNumber}")
    @PreAuthorize("hasAuthority('INVENTORY_TRACE_VIEW')")
    @Operation(summary = "Trace history by serial number")
    public ResponseEntity<ApiResponse<List<InventoryTraceEventResponse>>> getSerialTrace(@PathVariable String serialNumber) {
        List<InventoryTraceEventResponse> response = service.getSerialTrace(serialNumber);
        return ResponseEntity.ok(ApiResponse.success("Serial trace history retrieved successfully", response));
    }

    /**
     * Creates a new recall and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/inventory-recalls")
    @PreAuthorize("hasAuthority('INVENTORY_RECALL_CREATE')")
    @Operation(summary = "Initiate a product recall", description = "Quarantines/recalls target lot and its serials, logging auditing details")
    public ResponseEntity<ApiResponse<InventoryRecallResponse>> createRecall(
            @Valid @RequestBody InventoryRecallRequest request
    ) {
        InventoryRecallResponse response = service.createRecall(request);
        return new ResponseEntity<>(ApiResponse.success("Inventory recall initiated successfully", response), HttpStatus.CREATED);
    }

    /**
     * Retrieves a paginated list of all recalls records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/inventory-recalls")
    @PreAuthorize("hasAuthority('INVENTORY_RECALL_VIEW')")
    @Operation(summary = "List all inventory recalls")
    public ResponseEntity<ApiResponse<List<InventoryRecallResponse>>> getAllRecalls() {
        List<InventoryRecallResponse> response = service.getAllRecalls();
        return ResponseEntity.ok(ApiResponse.success("Inventory recalls retrieved successfully", response));
    }
}