/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.controller
 * File              : PickListController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickListController
 * Related Service   : PickListControllerService, PickListControllerServiceImpl
 * Related Repository: PickListControllerRepository
 * Related Entity    : PickListController
 * Related DTO       : ApiResponse, CompletePickingRequest, PickListRequest, PickListResponse, reasonRequest
 * Related Mapper    : PickListControllerMapper
 * Related DB Table  : pick_list_controllers
 * Related REST APIs : POST /api/v1/pick-lists, GET /api/v1/pick-lists/{id}, POST /api/v1/pick-lists/{id}/release, POST /api/v1/pick-lists/{id}/start-picking
 * Depends On        : Common Module
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Sales Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/pick-lists, GET /api/v1/pick-lists/{id}, POST /api/v1/pick-lists/{id}/release, POST /api/v1/pick-lists/{id}/start-picking
 ******************************************************************************/
package com.plus33.erp.sales.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.sales.dto.*;
import com.plus33.erp.sales.entity.PickListStatus;
import com.plus33.erp.sales.service.PickListService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code PickListController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to PickListService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> PickListController.endpoint()
 *   --> PickListService.method()
 *   --> PickListRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/pick-lists, GET /api/v1/pick-lists/{id}, POST /api/v1/pick-lists/{id}/release, POST /api/v1/pick-lists/{id}/start-picking, POST /api/v1/pick-lists/{id}/complete-picking</p>
 * <p><b>Module Deps      :</b> Common, Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/pick-lists")
@Tag(name = "Fulfillment & Picking Management", description = "REST APIs for managing pick lists and order fulfillment workflows")
public class PickListController {

    private final PickListService pickListService;

    public PickListController(PickListService pickListService) {
        this.pickListService = pickListService;
    }

    /**
     * Creates a new pick list and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('PICK_LIST_CREATE')")
    @Operation(summary = "Create a new pick list", description = "Initializes a pick list in DRAFT status for a specific sales order and location. Ensures idempotency via clientReferenceId.")
    public ResponseEntity<ApiResponse<PickListResponse>> createPickList(@Valid @RequestBody PickListRequest request) {
        PickListResponse response = pickListService.createPickList(request);
        return new ResponseEntity<>(ApiResponse.success("Pick list created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Retrieves a paginated list of pick list by id records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PICK_LIST_VIEW')")
    @Operation(summary = "Get pick list by ID", description = "Retrieves pick list details and line items by ID.")
    public ResponseEntity<ApiResponse<PickListResponse>> getPickListById(@PathVariable Long id) {
        PickListResponse response = pickListService.getPickListById(id);
        return ResponseEntity.ok(ApiResponse.success("Pick list retrieved successfully", response));
    }

    /**
     * Releases previously reserved pick list resources back to the available pool.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/release")
    @PreAuthorize("hasAuthority('PICK_LIST_RELEASE')")
    @Operation(summary = "Release pick list", description = "Allocates available physical stock and transitions status to RELEASED. Restricts to single active pick list per sales order + location.")
    public ResponseEntity<ApiResponse<PickListResponse>> releasePickList(@PathVariable Long id) {
        PickListResponse response = pickListService.releasePickList(id);
        return ResponseEntity.ok(ApiResponse.success("Pick list released successfully", response));
    }

    /**
     * Performs the startPicking operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/start-picking")
    @PreAuthorize("hasAuthority('PICK_LIST_PICK')")
    @Operation(summary = "Start picking items", description = "Transitions status from RELEASED to PICKING.")
    public ResponseEntity<ApiResponse<PickListResponse>> startPicking(@PathVariable Long id) {
        PickListResponse response = pickListService.startPicking(id);
        return ResponseEntity.ok(ApiResponse.success("Picking started successfully", response));
    }

    /**
     * Completes the picking workflow and finalizes the record status.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/complete-picking")
    @PreAuthorize("hasAuthority('PICK_LIST_PICK')")
    @Operation(summary = "Complete item picking", description = "Records picked quantities and transitions status from PICKING to PICKED. Quantities cannot exceed allocated amounts.")
    public ResponseEntity<ApiResponse<PickListResponse>> completePicking(
            @PathVariable Long id,
            @Valid @RequestBody CompletePickingRequest request
    ) {
        PickListResponse response = pickListService.completePicking(id, request);
        return ResponseEntity.ok(ApiResponse.success("Picking completed successfully", response));
    }

    /**
     * Performs the packPickList operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/pack")
    @PreAuthorize("hasAuthority('PICK_LIST_PACK')")
    @Operation(summary = "Pack pick list items", description = "Transitions status from PICKED to PACKED. Ready for shipment.")
    public ResponseEntity<ApiResponse<PickListResponse>> packPickList(@PathVariable Long id) {
        PickListResponse response = pickListService.packPickList(id);
        return ResponseEntity.ok(ApiResponse.success("Pick list packed successfully", response));
    }

    /**
     * Performs the shipPickList operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/{id}/ship")
    @PreAuthorize("hasAuthority('PICK_LIST_SHIP')")
    @Operation(summary = "Ship packed items", description = "Transitions status from PACKED to SHIPPED. Deducts inventory stock, releases remaining reservations, and updates sales order progress.")
    public ResponseEntity<ApiResponse<PickListResponse>> shipPickList(
            @PathVariable Long id,
            @Valid @RequestBody ShipRequest request
    ) {
        PickListResponse response = pickListService.shipPickList(id, request);
        return ResponseEntity.ok(ApiResponse.success("Pick list shipped successfully", response));
    }

    /**
     * Cancels the pick list and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('PICK_LIST_CANCEL')")
    @Operation(summary = "Cancel pick list", description = "Cancels the pick list and releases any active stock allocations.")
    public ResponseEntity<ApiResponse<PickListResponse>> cancelPickList(
            @PathVariable Long id,
            @Valid @RequestBody SalesOrderCancelRequest reasonRequest
    ) {
        PickListResponse response = pickListService.cancelPickList(id, reasonRequest.reason());
        return ResponseEntity.ok(ApiResponse.success("Pick list cancelled successfully", response));
    }

    /**
     * Retrieves a paginated list of pick lists by sales order id records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param salesOrderId the salesOrderId input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/order/{salesOrderId}")
    @PreAuthorize("hasAuthority('PICK_LIST_VIEW')")
    @Operation(summary = "Get pick lists by Sales Order ID", description = "Retrieves all pick lists associated with a sales order.")
    public ResponseEntity<ApiResponse<List<PickListResponse>>> getPickListsBySalesOrderId(@PathVariable Long salesOrderId) {
        List<PickListResponse> response = pickListService.getPickListsBySalesOrderId(salesOrderId);
        return ResponseEntity.ok(ApiResponse.success("Pick lists retrieved successfully", response));
    }

    /**
     * Retrieves a paginated list of pick lists by warehouse id records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param warehouseId the warehouseId input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/location/{warehouseId}")
    @PreAuthorize("hasAuthority('PICK_LIST_VIEW')")
    @Operation(summary = "Get pick lists by Warehouse ID", description = "Retrieves all pick lists associated with a warehouse.")
    public ResponseEntity<ApiResponse<List<PickListResponse>>> getPickListsByWarehouseId(@PathVariable Long warehouseId) {
        List<PickListResponse> response = pickListService.getPickListsByWarehouseId(warehouseId);
        return ResponseEntity.ok(ApiResponse.success("Pick lists retrieved successfully", response));
    }

    /**
     * Retrieves a paginated list of pick lists by status records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param status status filter for narrowing query results
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('PICK_LIST_VIEW')")
    @Operation(summary = "Get pick lists by Status", description = "Retrieves all pick lists with the specified status.")
    public ResponseEntity<ApiResponse<List<PickListResponse>>> getPickListsByStatus(@PathVariable PickListStatus status) {
        List<PickListResponse> response = pickListService.getPickListsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Pick lists retrieved successfully", response));
    }
}