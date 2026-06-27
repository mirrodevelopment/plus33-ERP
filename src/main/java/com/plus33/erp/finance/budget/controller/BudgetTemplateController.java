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

@RestController
@RequestMapping("/api/v1/budget-templates")
@RequiredArgsConstructor
@Tag(name = "Budget Templates", description = "API endpoints for managing reusable industry budget structures.")
public class BudgetTemplateController {

    private final BudgetTemplateService budgetTemplateService;

    @PostMapping
    @PreAuthorize("hasAuthority('BUDGET_CREATE')")
    @Operation(summary = "Create Budget Template", description = "Create a reusable budget line template.")
    public ResponseEntity<ApiResponse<BudgetTemplateResponse>> createTemplate(
            @Valid @RequestBody BudgetTemplateRequest request
    ) {
        BudgetTemplateResponse response = budgetTemplateService.createTemplate(request);
        return new ResponseEntity<>(ApiResponse.success("Budget template created successfully", response), HttpStatus.CREATED);
    }

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

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "Get Budget Template", description = "Retrieve structural details of a template.")
    public ResponseEntity<ApiResponse<BudgetTemplateResponse>> getTemplate(@PathVariable Long id) {
        BudgetTemplateResponse response = budgetTemplateService.getTemplate(id);
        return ResponseEntity.ok(ApiResponse.success("Budget template retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "List Budget Templates", description = "List all registered budget templates.")
    public ResponseEntity<ApiResponse<List<BudgetTemplateResponse>>> getAllTemplates() {
        List<BudgetTemplateResponse> response = budgetTemplateService.getAllTemplates();
        return ResponseEntity.ok(ApiResponse.success("Budget templates retrieved successfully", response));
    }
}
