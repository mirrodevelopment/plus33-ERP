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

@RestController
@RequestMapping("/api/v1/budget-policies")
@RequiredArgsConstructor
@Tag(name = "Budget Policies", description = "API endpoints for managing reusable budget control policies.")
public class BudgetPolicyController {

    private final BudgetPolicyService budgetPolicyService;

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

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "Get Budget Policy", description = "Retrieve a budget policy by its unique ID.")
    public ResponseEntity<ApiResponse<BudgetPolicyResponse>> getPolicy(@PathVariable Long id) {
        BudgetPolicyResponse response = budgetPolicyService.getPolicy(id);
        return ResponseEntity.ok(ApiResponse.success("Budget policy retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "List Budget Policies", description = "List all budget policies for a company.")
    public ResponseEntity<ApiResponse<List<BudgetPolicyResponse>>> getPoliciesByCompany(@RequestParam Long companyId) {
        List<BudgetPolicyResponse> response = budgetPolicyService.getPoliciesByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Budget policies retrieved successfully", response));
    }
}
