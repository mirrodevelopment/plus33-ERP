/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.controller
 * File              : WarehouseController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseController
 * Related Service   : WarehouseControllerService, WarehouseControllerServiceImpl
 * Related Repository: WarehouseControllerRepository
 * Related Entity    : WarehouseController
 * Related DTO       : ApiResponse, PageRequest, PageResponse, searchRequest, WarehouseRequest
 * Related Mapper    : WarehouseControllerMapper
 * Related DB Table  : warehouse_controllers
 * Related REST APIs : POST /api/v1/warehouses, GET /api/v1/warehouses/{id}, GET /api/v1/warehouses, PUT /api/v1/warehouses/{id}
 * Depends On        : Common Module
 * Used By           : Organization Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Organization Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/warehouses, GET /api/v1/warehouses/{id}, GET /api/v1/warehouses, PUT /api/v1/warehouses/{id}
 ******************************************************************************/
package com.plus33.erp.organization.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.organization.dto.WarehouseRequest;
import com.plus33.erp.organization.dto.WarehouseResponse;
import com.plus33.erp.organization.dto.WarehouseSearchRequest;
import com.plus33.erp.organization.service.WarehouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to WarehouseService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> WarehouseController.endpoint()
 *   --> WarehouseService.method()
 *   --> WarehouseRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/warehouses, GET /api/v1/warehouses/{id}, GET /api/v1/warehouses, PUT /api/v1/warehouses/{id}, DELETE /api/v1/warehouses/{id}</p>
 * <p><b>Module Deps      :</b> Common, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/warehouses")
@Tag(name = "Warehouse Management", description = "REST APIs for managing organization warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    /**
     * Creates a new warehouse and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('WAREHOUSE_CREATE')")
    @Operation(summary = "Create a new warehouse", description = "Adds a new warehouse scoped to a region. Code must be unique within the region.")
    public ResponseEntity<ApiResponse<WarehouseResponse>> createWarehouse(@Valid @RequestBody WarehouseRequest request) {
        WarehouseResponse response = warehouseService.createWarehouse(request);
        return new ResponseEntity<>(ApiResponse.success("Warehouse created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single warehouse by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_VIEW')")
    @Operation(summary = "Get warehouse by ID", description = "Retrieves details of a warehouse by its primary key.")
    public ResponseEntity<ApiResponse<WarehouseResponse>> getWarehouseById(@PathVariable Long id) {
        WarehouseResponse response = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(ApiResponse.success("Warehouse retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of warehouses records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('WAREHOUSE_VIEW')")
    @Operation(summary = "Search warehouses", description = "Performs dynamic searches and pagination filters for warehouses.")
    public ResponseEntity<ApiResponse<PageResponse<WarehouseResponse>>> searchWarehouses(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        WarehouseSearchRequest searchRequest = new WarehouseSearchRequest(query, companyId, regionId, active);
        PageResponse<WarehouseResponse> response = warehouseService.searchWarehouses(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Warehouses retrieved successfully", response));
    }

    /**
     * Updates an existing warehouse record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_UPDATE')")
    @Operation(summary = "Update warehouse details", description = "Modifies details of an active warehouse by ID.")
    public ResponseEntity<ApiResponse<WarehouseResponse>> updateWarehouse(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseRequest request
    ) {
        WarehouseResponse response = warehouseService.updateWarehouse(id, request);
        return ResponseEntity.ok(ApiResponse.success("Warehouse updated successfully", response));
    }

    /**
     * Permanently deletes the warehouse from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_DELETE')")
    @Operation(summary = "Soft delete warehouse", description = "Flags active field to false to soft delete a warehouse.")
    public ResponseEntity<ApiResponse<Void>> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.ok(ApiResponse.success("Warehouse deleted successfully", null));
    }

    /**
     * Performs the activateWarehouse operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('WAREHOUSE_UPDATE')")
    @Operation(summary = "Activate warehouse", description = "Activates an inactive warehouse.")
    public ResponseEntity<ApiResponse<WarehouseResponse>> activateWarehouse(@PathVariable Long id) {
        WarehouseResponse response = warehouseService.activateWarehouse(id);
        return ResponseEntity.ok(ApiResponse.success("Warehouse activated successfully", response));
    }

    /**
     * Performs the deactivateWarehouse operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('WAREHOUSE_UPDATE')")
    @Operation(summary = "Deactivate warehouse", description = "Deactivates a warehouse.")
    public ResponseEntity<ApiResponse<WarehouseResponse>> deactivateWarehouse(@PathVariable Long id) {
        WarehouseResponse response = warehouseService.deactivateWarehouse(id);
        return ResponseEntity.ok(ApiResponse.success("Warehouse deactivated successfully", response));
    }
}