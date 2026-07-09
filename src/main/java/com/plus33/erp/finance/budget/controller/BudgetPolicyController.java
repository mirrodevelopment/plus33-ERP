/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.controller
 * File              : BudgetPolicyController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetPolicyController
 * Related Service   : BudgetPolicyControllerService, BudgetPolicyControllerServiceImpl
 * Related Repository: BudgetPolicyControllerRepository
 * Related Entity    : BudgetPolicyController
 * Related DTO       : ApiResponse, BudgetPolicyRequest, BudgetPolicyResponse
 * Related Mapper    : BudgetPolicyControllerMapper
 * Related DB Table  : budget_policy_controllers
 * Related REST APIs : POST /api/v1/budget-policies, PUT /api/v1/budget-policies/{id}, GET /api/v1/budget-policies/{id}, GET /api/v1/budget-policies
 * Depends On        : Common Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Finance Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/budget-policies, PUT /api/v1/budget-policies/{id}, GET /api/v1/budget-policies/{id}, GET /api/v1/budget-policies
 ******************************************************************************/
package com.plus33.erp.finance.budget.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.budget.dto.BudgetPolicyRequest;
import com.plus33.erp.finance.budget.dto.BudgetPolicyResponse;
import com.plus33.erp.finance.budget.service.BudgetPolicyService;
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
 * <p><b>Class  :</b> {@code BudgetPolicyController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to BudgetPolicyService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> BudgetPolicyController.endpoint()
 *   --> BudgetPolicyService.method()
 *   --> BudgetPolicyRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/budget-policies, PUT /api/v1/budget-policies/{id}, GET /api/v1/budget-policies/{id}, GET /api/v1/budget-policies</p>
 * <p><b>Module Deps      :</b> Common, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/budget-policies")
@RequiredArgsConstructor
@Tag(name = "Budget Policies", description = "API endpoints for managing reusable budget control policies.")
public class BudgetPolicyController {

    private final BudgetPolicyService budgetPolicyService;

    /**
     * Creates a new policy and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('BUDGET_CREATE')")
    @Operation(summary = "Create Budget Policy", description = "Create a new budget policy governing control behavior.")
    public ResponseEntity<ApiResponse<BudgetPolicyResponse>> createPolicy(
            @RequestParam Long companyId,
            @Valid @RequestBody BudgetPolicyRequest request
    ) {
        BudgetPolicyResponse response = budgetPolicyService.createPolicy(companyId, request);
        return new ResponseEntity<>(ApiResponse.success("Budget policy created successfully", response), HttpStatus.CREATED);
    }

    /**
     * Updates an existing policy record in the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_CREATE')")
    @Operation(summary = "Update Budget Policy", description = "Update behavior flags and settings of a budget policy.")
    public ResponseEntity<ApiResponse<BudgetPolicyResponse>> updatePolicy(
            @PathVariable Long id,
            @Valid @RequestBody BudgetPolicyRequest request
    ) {
        BudgetPolicyResponse response = budgetPolicyService.updatePolicy(id, request);
        return ResponseEntity.ok(ApiResponse.success("Budget policy updated successfully", response));
    }

    /**
     * Retrieves policy data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "Get Budget Policy", description = "Retrieve a budget policy by its unique ID.")
    public ResponseEntity<ApiResponse<BudgetPolicyResponse>> getPolicy(@PathVariable Long id) {
        BudgetPolicyResponse response = budgetPolicyService.getPolicy(id);
        return ResponseEntity.ok(ApiResponse.success("Budget policy retrieved successfully", response));
    }

    /**
     * Retrieves policies by company data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "List Budget Policies", description = "List all budget policies for a company.")
    public ResponseEntity<ApiResponse<List<BudgetPolicyResponse>>> getPoliciesByCompany(@RequestParam Long companyId) {
        List<BudgetPolicyResponse> response = budgetPolicyService.getPoliciesByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Budget policies retrieved successfully", response));
    }
}