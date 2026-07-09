/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.controller
 * File              : DepartmentController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DepartmentController
 * Related Service   : DepartmentControllerService, DepartmentControllerServiceImpl
 * Related Repository: DepartmentControllerRepository
 * Related Entity    : DepartmentController
 * Related DTO       : ApiResponse, DepartmentRequest, DepartmentResponse
 * Related Mapper    : DepartmentControllerMapper
 * Related DB Table  : department_controllers
 * Related REST APIs : POST /api/v1/departments, PUT /api/v1/departments/{id}, GET /api/v1/departments/{id}, GET /api/v1/departments
 * Depends On        : Common Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Finance Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/departments, PUT /api/v1/departments/{id}, GET /api/v1/departments/{id}, GET /api/v1/departments
 ******************************************************************************/
package com.plus33.erp.finance.budget.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.budget.dto.DepartmentRequest;
import com.plus33.erp.finance.budget.dto.DepartmentResponse;
import com.plus33.erp.finance.budget.service.DepartmentService;
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
 * <p><b>Class  :</b> {@code DepartmentController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to DepartmentService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> DepartmentController.endpoint()
 *   --> DepartmentService.method()
 *   --> DepartmentRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/departments, PUT /api/v1/departments/{id}, GET /api/v1/departments/{id}, GET /api/v1/departments</p>
 * <p><b>Module Deps      :</b> Common, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Tag(name = "Budget Departments", description = "API endpoints for managing organizational departments.")
public class DepartmentController {

    private final DepartmentService departmentService;

    /**
     * Creates a new department and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('BUDGET_CREATE')")
    @Operation(summary = "Create Department", description = "Create a new department for a company.")
    public ResponseEntity<ApiResponse<DepartmentResponse>> createDepartment(
            @RequestParam Long companyId,
            @Valid @RequestBody DepartmentRequest request
    ) {
        DepartmentResponse response = departmentService.createDepartment(companyId, request);
        return new ResponseEntity<>(ApiResponse.success("Department created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Updates an existing department record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_CREATE')")
    @Operation(summary = "Update Department", description = "Update name or active status of a department.")
    public ResponseEntity<ApiResponse<DepartmentResponse>> updateDepartment(
            @PathVariable Long id,
            @Valid @RequestBody DepartmentRequest request
    ) {
        DepartmentResponse response = departmentService.updateDepartment(id, request);
        return ResponseEntity.ok(ApiResponse.success("Department updated successfully", response));
    }

    /**
     * Retrieves department data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "Get Department", description = "Retrieve a department by its unique ID.")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartment(@PathVariable Long id) {
        DepartmentResponse response = departmentService.getDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department retrieved successfully", response));
    }

    /**
     * Retrieves departments by company data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "List Departments", description = "List all departments for a company.")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getDepartmentsByCompany(@RequestParam Long companyId) {
        List<DepartmentResponse> response = departmentService.getDepartmentsByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Departments retrieved successfully", response));
    }
}