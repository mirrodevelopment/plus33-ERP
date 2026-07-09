/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.controller
 * File              : CostCenterController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CostCenterController
 * Related Service   : CostCenterControllerService, CostCenterControllerServiceImpl
 * Related Repository: CostCenterControllerRepository
 * Related Entity    : CostCenterController
 * Related DTO       : ApiResponse, CostCenterRequest, CostCenterResponse
 * Related Mapper    : CostCenterControllerMapper
 * Related DB Table  : cost_center_controllers
 * Related REST APIs : POST /api/v1/cost-centers, PUT /api/v1/cost-centers/{id}, GET /api/v1/cost-centers/{id}, GET /api/v1/cost-centers
 * Depends On        : Common Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Finance Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/cost-centers, PUT /api/v1/cost-centers/{id}, GET /api/v1/cost-centers/{id}, GET /api/v1/cost-centers
 ******************************************************************************/
package com.plus33.erp.finance.budget.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.budget.dto.CostCenterRequest;
import com.plus33.erp.finance.budget.dto.CostCenterResponse;
import com.plus33.erp.finance.budget.service.CostCenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code CostCenterController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to CostCenterService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> CostCenterController.endpoint()
 *   --> CostCenterService.method()
 *   --> CostCenterRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/cost-centers, PUT /api/v1/cost-centers/{id}, GET /api/v1/cost-centers/{id}, GET /api/v1/cost-centers</p>
 * <p><b>Module Deps      :</b> Common, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/cost-centers")
@RequiredArgsConstructor
@Tag(name = "Budget Cost Centers", description = "API endpoints for managing cost centers.")
public class CostCenterController {

    private final CostCenterService costCenterService;

    /**
     * Creates a new cost center and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('BUDGET_CREATE')")
    @Operation(summary = "Create Cost Center", description = "Create a new cost center for a company.")
    public ResponseEntity<ApiResponse<CostCenterResponse>> createCostCenter(
            @RequestParam Long companyId,
            @Valid @RequestBody CostCenterRequest request
    ) {
        CostCenterResponse response = costCenterService.createCostCenter(companyId, request);
        return new ResponseEntity<>(ApiResponse.success("Cost center created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Updates an existing cost center record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_CREATE')")
    @Operation(summary = "Update Cost Center", description = "Update name or active status of a cost center.")
    public ResponseEntity<ApiResponse<CostCenterResponse>> updateCostCenter(
            @PathVariable Long id,
            @Valid @RequestBody CostCenterRequest request
    ) {
        CostCenterResponse response = costCenterService.updateCostCenter(id, request);
        return ResponseEntity.ok(ApiResponse.success("Cost center updated successfully", response));
    }

    /**
     * Retrieves cost center data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "Get Cost Center", description = "Retrieve a cost center by its unique ID.")
    public ResponseEntity<ApiResponse<CostCenterResponse>> getCostCenter(@PathVariable Long id) {
        CostCenterResponse response = costCenterService.getCostCenter(id);
        return ResponseEntity.ok(ApiResponse.success("Cost center retrieved successfully", response));
    }

    /**
     * Retrieves cost centers by company data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "List Cost Centers", description = "List all cost centers for a company.")
    public ResponseEntity<ApiResponse<List<CostCenterResponse>>> getCostCentersByCompany(@RequestParam Long companyId) {
        List<CostCenterResponse> response = costCenterService.getCostCentersByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Cost centers retrieved successfully", response));
    }
}