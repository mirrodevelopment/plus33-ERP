/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.controller
 * File              : ProjectController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectController
 * Related Service   : ProjectControllerService, ProjectControllerServiceImpl
 * Related Repository: ProjectControllerRepository
 * Related Entity    : ProjectController
 * Related DTO       : ApiResponse, ProjectRequest, ProjectResponse
 * Related Mapper    : ProjectControllerMapper
 * Related DB Table  : project_controllers
 * Related REST APIs : POST /api/v1/projects, PUT /api/v1/projects/{id}, GET /api/v1/projects/{id}, GET /api/v1/projects
 * Depends On        : Common Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Finance Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/projects, PUT /api/v1/projects/{id}, GET /api/v1/projects/{id}, GET /api/v1/projects
 ******************************************************************************/
package com.plus33.erp.finance.budget.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.budget.dto.ProjectRequest;
import com.plus33.erp.finance.budget.dto.ProjectResponse;
import com.plus33.erp.finance.budget.service.ProjectService;
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
 * <p><b>Class  :</b> {@code ProjectController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to ProjectService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> ProjectController.endpoint()
 *   --> ProjectService.method()
 *   --> ProjectRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/projects, PUT /api/v1/projects/{id}, GET /api/v1/projects/{id}, GET /api/v1/projects</p>
 * <p><b>Module Deps      :</b> Common, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Budget Projects", description = "API endpoints for managing organizational projects.")
public class ProjectController {

    private final ProjectService projectService;

    /**
     * Creates a new project and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('BUDGET_CREATE')")
    @Operation(summary = "Create Project", description = "Create a new project for a company.")
    public ResponseEntity<ApiResponse<ProjectResponse>> createProject(
            @RequestParam Long companyId,
            @Valid @RequestBody ProjectRequest request
    ) {
        ProjectResponse response = projectService.createProject(companyId, request);
        return new ResponseEntity<>(ApiResponse.success("Project created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Updates an existing project record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_CREATE')")
    @Operation(summary = "Update Project", description = "Update project details or status.")
    public ResponseEntity<ApiResponse<ProjectResponse>> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request
    ) {
        ProjectResponse response = projectService.updateProject(id, request);
        return ResponseEntity.ok(ApiResponse.success("Project updated successfully", response));
    }

    /**
     * Retrieves project data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "Get Project", description = "Retrieve a project by its unique ID.")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProject(@PathVariable Long id) {
        ProjectResponse response = projectService.getProject(id);
        return ResponseEntity.ok(ApiResponse.success("Project retrieved successfully", response));
    }

    /**
     * Retrieves projects by company data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "List Projects", description = "List all projects for a company.")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getProjectsByCompany(@RequestParam Long companyId) {
        List<ProjectResponse> response = projectService.getProjectsByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Projects retrieved successfully", response));
    }
}