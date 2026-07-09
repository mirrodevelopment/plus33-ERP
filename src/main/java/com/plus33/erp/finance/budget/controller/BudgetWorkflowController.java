/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.controller
 * File              : BudgetWorkflowController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetWorkflowController
 * Related Service   : BudgetWorkflowControllerService, BudgetWorkflowControllerServiceImpl
 * Related Repository: BudgetWorkflowControllerRepository
 * Related Entity    : BudgetWorkflowController
 * Related DTO       : ApiResponse, BudgetWorkflowTemplateRequest, BudgetWorkflowTemplateResponse
 * Related Mapper    : BudgetWorkflowControllerMapper
 * Related DB Table  : budget_workflow_controllers
 * Related REST APIs : POST /api/v1/budget-workflows, PUT /api/v1/budget-workflows/{id}, GET /api/v1/budget-workflows/{id}, GET /api/v1/budget-workflows
 * Depends On        : Common Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Finance Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/budget-workflows, PUT /api/v1/budget-workflows/{id}, GET /api/v1/budget-workflows/{id}, GET /api/v1/budget-workflows
 ******************************************************************************/
package com.plus33.erp.finance.budget.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.budget.dto.BudgetWorkflowTemplateRequest;
import com.plus33.erp.finance.budget.dto.BudgetWorkflowTemplateResponse;
import com.plus33.erp.finance.budget.service.BudgetWorkflowService;
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
 * <p><b>Class  :</b> {@code BudgetWorkflowController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to BudgetWorkflowService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> BudgetWorkflowController.endpoint()
 *   --> BudgetWorkflowService.method()
 *   --> BudgetWorkflowRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/budget-workflows, PUT /api/v1/budget-workflows/{id}, GET /api/v1/budget-workflows/{id}, GET /api/v1/budget-workflows</p>
 * <p><b>Module Deps      :</b> Common, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/budget-workflows")
@RequiredArgsConstructor
@Tag(name = "Budget Workflows", description = "API endpoints for managing multi-step approval workflows.")
public class BudgetWorkflowController {

    private final BudgetWorkflowService workflowService;

    /**
     * Creates a new template and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('BUDGET_CREATE')")
    @Operation(summary = "Create Workflow Template", description = "Configure a multi-step approval workflow.")
    public ResponseEntity<ApiResponse<BudgetWorkflowTemplateResponse>> createTemplate(
            @RequestParam Long companyId,
            @Valid @RequestBody BudgetWorkflowTemplateRequest request
    ) {
        BudgetWorkflowTemplateResponse response = workflowService.createTemplate(companyId, request);
        return new ResponseEntity<>(ApiResponse.success("Workflow template created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Updates an existing template record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_CREATE')")
    @Operation(summary = "Update Workflow Template", description = "Modify steps, sequencing, or role routing of a workflow.")
    public ResponseEntity<ApiResponse<BudgetWorkflowTemplateResponse>> updateTemplate(
            @PathVariable Long id,
            @Valid @RequestBody BudgetWorkflowTemplateRequest request
    ) {
        BudgetWorkflowTemplateResponse response = workflowService.updateTemplate(id, request);
        return ResponseEntity.ok(ApiResponse.success("Workflow template updated successfully", response));
    }

    /**
     * Retrieves template data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "Get Workflow Template", description = "Retrieve a workflow config template by ID.")
    public ResponseEntity<ApiResponse<BudgetWorkflowTemplateResponse>> getTemplate(@PathVariable Long id) {
        BudgetWorkflowTemplateResponse response = workflowService.getTemplate(id);
        return ResponseEntity.ok(ApiResponse.success("Workflow template retrieved successfully", response));
    }

    /**
     * Retrieves templates by company data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "List Workflow Templates", description = "List all workflows configured for a company.")
    public ResponseEntity<ApiResponse<List<BudgetWorkflowTemplateResponse>>> getTemplatesByCompany(@RequestParam Long companyId) {
        List<BudgetWorkflowTemplateResponse> response = workflowService.getTemplatesByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Workflow templates retrieved successfully", response));
    }
}