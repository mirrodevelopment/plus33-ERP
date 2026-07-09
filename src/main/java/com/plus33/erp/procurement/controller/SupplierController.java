/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.controller
 * File              : SupplierController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierController
 * Related Service   : SupplierControllerService, SupplierControllerServiceImpl
 * Related Repository: SupplierControllerRepository
 * Related Entity    : SupplierController
 * Related DTO       : ApiResponse, PageRequest, PageResponse, searchRequest, SupplierRequest
 * Related Mapper    : SupplierControllerMapper
 * Related DB Table  : supplier_controllers
 * Related REST APIs : POST /api/v1/suppliers, GET /api/v1/suppliers/{id}, GET /api/v1/suppliers, PUT /api/v1/suppliers/{id}
 * Depends On        : Common Module
 * Used By           : Procurement Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Procurement Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/suppliers, GET /api/v1/suppliers/{id}, GET /api/v1/suppliers, PUT /api/v1/suppliers/{id}
 ******************************************************************************/
package com.plus33.erp.procurement.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.procurement.dto.SupplierRequest;
import com.plus33.erp.procurement.dto.SupplierResponse;
import com.plus33.erp.procurement.dto.SupplierSearchRequest;
import com.plus33.erp.procurement.service.SupplierService;
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
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to SupplierService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> SupplierController.endpoint()
 *   --> SupplierService.method()
 *   --> SupplierRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/suppliers, GET /api/v1/suppliers/{id}, GET /api/v1/suppliers, PUT /api/v1/suppliers/{id}, PATCH /api/v1/suppliers/{id}/activate</p>
 * <p><b>Module Deps      :</b> Common, Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/suppliers")
@Tag(name = "Supplier Management", description = "REST APIs for managing procurement suppliers")
public class SupplierController {

    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    /**
     * Creates a new supplier and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('SUPPLIER_CREATE')")
    @Operation(summary = "Create a new supplier", description = "Adds a new supplier scoped to a company. Code and email must be unique within the company.")
    public ResponseEntity<ApiResponse<SupplierResponse>> createSupplier(@Valid @RequestBody SupplierRequest request) {
        SupplierResponse response = supplierService.createSupplier(request);
        return new ResponseEntity<>(ApiResponse.success("Supplier created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single supplier by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER_VIEW')")
    @Operation(summary = "Get supplier by ID", description = "Retrieves details of a supplier by its primary key.")
    public ResponseEntity<ApiResponse<SupplierResponse>> getSupplierById(@PathVariable Long id) {
        SupplierResponse response = supplierService.getSupplierById(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of suppliers records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('SUPPLIER_VIEW')")
    @Operation(summary = "Search suppliers", description = "Performs dynamic searches and pagination filters for suppliers.")
    public ResponseEntity<ApiResponse<PageResponse<SupplierResponse>>> searchSuppliers(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        SupplierSearchRequest searchRequest = new SupplierSearchRequest(query, companyId, active);
        PageResponse<SupplierResponse> response = supplierService.searchSuppliers(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Suppliers retrieved successfully", response));
    }

    /**
     * Updates an existing supplier record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER_UPDATE')")
    @Operation(summary = "Update supplier details", description = "Modifies details of an active supplier by ID.")
    public ResponseEntity<ApiResponse<SupplierResponse>> updateSupplier(
            @PathVariable Long id,
            @Valid @RequestBody SupplierRequest request
    ) {
        SupplierResponse response = supplierService.updateSupplier(id, request);
        return ResponseEntity.ok(ApiResponse.success("Supplier updated successfully", response));
    }

    /**
     * Performs the activateSupplier operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('SUPPLIER_UPDATE')")
    @Operation(summary = "Activate supplier", description = "Activates a soft-deleted or inactive supplier.")
    public ResponseEntity<ApiResponse<SupplierResponse>> activateSupplier(@PathVariable Long id) {
        SupplierResponse response = supplierService.activateSupplier(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier activated successfully", response));
    }

    /**
     * Performs the deactivateSupplier operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('SUPPLIER_UPDATE')")
    @Operation(summary = "Deactivate supplier", description = "Deactivates a supplier without complete deletion.")
    public ResponseEntity<ApiResponse<SupplierResponse>> deactivateSupplier(@PathVariable Long id) {
        SupplierResponse response = supplierService.deactivateSupplier(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier deactivated successfully", response));
    }

    /**
     * Permanently deletes the supplier from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('SUPPLIER_DELETE')")
    @Operation(summary = "Soft delete supplier", description = "Flags active field to false to soft delete a supplier.")
    public ResponseEntity<ApiResponse<Void>> deleteSupplier(@PathVariable Long id) {
        supplierService.deleteSupplier(id);
        return ResponseEntity.ok(ApiResponse.success("Supplier deleted successfully", null));
    }
}