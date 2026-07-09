/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.controller
 * File              : RegionController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RegionController
 * Related Service   : RegionControllerService, RegionControllerServiceImpl
 * Related Repository: RegionControllerRepository
 * Related Entity    : RegionController
 * Related DTO       : ApiResponse, PageRequest, PageResponse, RegionRequest, RegionResponse
 * Related Mapper    : RegionControllerMapper
 * Related DB Table  : region_controllers
 * Related REST APIs : POST /api/v1/regions, GET /api/v1/regions/{id}, GET /api/v1/regions, PUT /api/v1/regions/{id}
 * Depends On        : Common Module
 * Used By           : Organization Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Organization Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/regions, GET /api/v1/regions/{id}, GET /api/v1/regions, PUT /api/v1/regions/{id}
 ******************************************************************************/
package com.plus33.erp.organization.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.organization.dto.RegionRequest;
import com.plus33.erp.organization.dto.RegionResponse;
import com.plus33.erp.organization.dto.RegionSearchRequest;
import com.plus33.erp.organization.service.RegionService;
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
 * <p><b>Class  :</b> {@code RegionController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to RegionService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> RegionController.endpoint()
 *   --> RegionService.method()
 *   --> RegionRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/regions, GET /api/v1/regions/{id}, GET /api/v1/regions, PUT /api/v1/regions/{id}, DELETE /api/v1/regions/{id}</p>
 * <p><b>Module Deps      :</b> Common, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/regions")
@Tag(name = "Region Management", description = "REST APIs for managing organization regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    /**
     * Creates a new region and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('REGION_CREATE')")
    @Operation(summary = "Create a new region", description = "Adds a new region scoped to a company. Code must be unique within the company.")
    public ResponseEntity<ApiResponse<RegionResponse>> createRegion(@Valid @RequestBody RegionRequest request) {
        RegionResponse response = regionService.createRegion(request);
        return new ResponseEntity<>(ApiResponse.success("Region created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single region by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('REGION_VIEW')")
    @Operation(summary = "Get region by ID", description = "Retrieves details of a region by its primary key.")
    public ResponseEntity<ApiResponse<RegionResponse>> getRegionById(@PathVariable Long id) {
        RegionResponse response = regionService.getRegionById(id);
        return ResponseEntity.ok(ApiResponse.success("Region retrieved successfully", response));
    }

    /**
     * Returns a filtered paginated list of regions records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @GetMapping
    @PreAuthorize("hasAuthority('REGION_VIEW')")
    @Operation(summary = "Search regions", description = "Performs dynamic searches and pagination filters for regions.")
    public ResponseEntity<ApiResponse<PageResponse<RegionResponse>>> searchRegions(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        RegionSearchRequest searchRequest = new RegionSearchRequest(query, companyId);
        PageResponse<RegionResponse> response = regionService.searchRegions(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Regions retrieved successfully", response));
    }

    /**
     * Updates an existing region record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('REGION_UPDATE')")
    @Operation(summary = "Update region details", description = "Modifies details of an active region by ID.")
    public ResponseEntity<ApiResponse<RegionResponse>> updateRegion(
            @PathVariable Long id,
            @Valid @RequestBody RegionRequest request
    ) {
        RegionResponse response = regionService.updateRegion(id, request);
        return ResponseEntity.ok(ApiResponse.success("Region updated successfully", response));
    }

    /**
     * Permanently deletes the region from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('REGION_DELETE')")
    @Operation(summary = "Delete region", description = "Performs hard-delete of a region by ID if no active stores, warehouses, or employees depend on it.")
    public ResponseEntity<ApiResponse<Void>> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.ok(ApiResponse.success("Region deleted successfully", null));
    }
}