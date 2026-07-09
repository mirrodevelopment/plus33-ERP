/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.controller
 * File              : BudgetTemplateController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetTemplateController
 * Related Service   : BudgetTemplateControllerService, BudgetTemplateControllerServiceImpl
 * Related Repository: BudgetTemplateControllerRepository
 * Related Entity    : BudgetTemplateController
 * Related DTO       : ApiResponse, BudgetTemplateRequest, BudgetTemplateResponse
 * Related Mapper    : BudgetTemplateControllerMapper
 * Related DB Table  : budget_template_controllers
 * Related REST APIs : POST /api/v1/budget-templates, PUT /api/v1/budget-templates/{id}, GET /api/v1/budget-templates/{id}, GET /api/v1/budget-templates
 * Depends On        : Common Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Finance Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/budget-templates, PUT /api/v1/budget-templates/{id}, GET /api/v1/budget-templates/{id}, GET /api/v1/budget-templates
 ******************************************************************************/
package com.plus33.erp.finance.budget.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.budget.dto.BudgetTemplateRequest;
import com.plus33.erp.finance.budget.dto.BudgetTemplateResponse;
import com.plus33.erp.finance.budget.service.BudgetTemplateService;
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
 * <p><b>Class  :</b> {@code BudgetTemplateController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to BudgetTemplateService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> BudgetTemplateController.endpoint()
 *   --> BudgetTemplateService.method()
 *   --> BudgetTemplateRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/budget-templates, PUT /api/v1/budget-templates/{id}, GET /api/v1/budget-templates/{id}, GET /api/v1/budget-templates</p>
 * <p><b>Module Deps      :</b> Common, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/budget-templates")
@RequiredArgsConstructor
@Tag(name = "Budget Templates", description = "API endpoints for managing reusable industry budget structures.")
public class BudgetTemplateController {

    private final BudgetTemplateService budgetTemplateService;

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
    @Operation(summary = "Create Budget Template", description = "Create a reusable budget line template.")
    public ResponseEntity<ApiResponse<BudgetTemplateResponse>> createTemplate(
            @Valid @RequestBody BudgetTemplateRequest request
    ) {
        BudgetTemplateResponse response = budgetTemplateService.createTemplate(request);
        return new ResponseEntity<>(ApiResponse.success("Budget template created successfully", response), HttpStatus.CREATED);
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
    @Operation(summary = "Update Budget Template", description = "Update structural lines of a budget template.")
    public ResponseEntity<ApiResponse<BudgetTemplateResponse>> updateTemplate(
            @PathVariable Long id,
            @Valid @RequestBody BudgetTemplateRequest request
    ) {
        BudgetTemplateResponse response = budgetTemplateService.updateTemplate(id, request);
        return ResponseEntity.ok(ApiResponse.success("Budget template updated successfully", response));
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
    @Operation(summary = "Get Budget Template", description = "Retrieve structural details of a template.")
    public ResponseEntity<ApiResponse<BudgetTemplateResponse>> getTemplate(@PathVariable Long id) {
        BudgetTemplateResponse response = budgetTemplateService.getTemplate(id);
        return ResponseEntity.ok(ApiResponse.success("Budget template retrieved successfully", response));
    }

    /**
     * Retrieves a paginated list of all templates records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "List Budget Templates", description = "List all registered budget templates.")
    public ResponseEntity<ApiResponse<List<BudgetTemplateResponse>>> getAllTemplates() {
        List<BudgetTemplateResponse> response = budgetTemplateService.getAllTemplates();
        return ResponseEntity.ok(ApiResponse.success("Budget templates retrieved successfully", response));
    }
}