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

@RestController
@RequestMapping("/api/v1/budget-workflows")
@RequiredArgsConstructor
@Tag(name = "Budget Workflows", description = "API endpoints for managing multi-step approval workflows.")
public class BudgetWorkflowController {

    private final BudgetWorkflowService workflowService;

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

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "Get Workflow Template", description = "Retrieve a workflow config template by ID.")
    public ResponseEntity<ApiResponse<BudgetWorkflowTemplateResponse>> getTemplate(@PathVariable Long id) {
        BudgetWorkflowTemplateResponse response = workflowService.getTemplate(id);
        return ResponseEntity.ok(ApiResponse.success("Workflow template retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "List Workflow Templates", description = "List all workflows configured for a company.")
    public ResponseEntity<ApiResponse<List<BudgetWorkflowTemplateResponse>>> getTemplatesByCompany(@RequestParam Long companyId) {
        List<BudgetWorkflowTemplateResponse> response = workflowService.getTemplatesByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Workflow templates retrieved successfully", response));
    }
}
