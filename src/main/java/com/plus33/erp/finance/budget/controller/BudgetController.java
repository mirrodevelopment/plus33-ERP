package com.plus33.erp.finance.budget.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.budget.dto.*;
import com.plus33.erp.finance.budget.service.BudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/budgets")
@RequiredArgsConstructor
@Tag(name = "Budgeting & Financial Planning", description = "API endpoints for managing budgets, revisions, workflows, and spend control.")
public class BudgetController {

    private final BudgetService budgetService;

    @PostMapping
    @PreAuthorize("hasAuthority('BUDGET_CREATE')")
    @Operation(summary = "Create Budget", description = "Draft a new budget plan and allocate lines.")
    public ResponseEntity<ApiResponse<BudgetResponse>> createBudget(
            @RequestParam Long companyId,
            @Valid @RequestBody BudgetRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "anonymous";
        BudgetResponse response = budgetService.createBudget(companyId, request, username);
        return new ResponseEntity<>(ApiResponse.success("Budget plan created successfully", response), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_CREATE')")
    @Operation(summary = "Update Budget", description = "Update headers and lines of a draft budget plan.")
    public ResponseEntity<ApiResponse<BudgetResponse>> updateBudget(
            @PathVariable Long id,
            @Valid @RequestBody BudgetRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "anonymous";
        BudgetResponse response = budgetService.updateBudget(id, request, username);
        return ResponseEntity.ok(ApiResponse.success("Budget plan updated successfully", response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "Get Budget", description = "Retrieve a budget and all its child allocations.")
    public ResponseEntity<ApiResponse<BudgetResponse>> getBudget(@PathVariable Long id) {
        BudgetResponse response = budgetService.getBudget(id);
        return ResponseEntity.ok(ApiResponse.success("Budget plan retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "List Budgets", description = "List all budgets registered for a company.")
    public ResponseEntity<ApiResponse<List<BudgetResponse>>> getBudgetsByCompany(@RequestParam Long companyId) {
        List<BudgetResponse> response = budgetService.getBudgetsByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Budget plans retrieved successfully", response));
    }

    @PostMapping("/{id}/submit")
    @PreAuthorize("hasAuthority('BUDGET_CREATE')")
    @Operation(summary = "Submit Budget for Approval", description = "Submit a DRAFT budget plan, launching the approval workflow.")
    public ResponseEntity<ApiResponse<BudgetResponse>> submitBudget(@PathVariable Long id, Principal principal) {
        String username = principal != null ? principal.getName() : "anonymous";
        BudgetResponse response = budgetService.submitBudget(id, username);
        return ResponseEntity.ok(ApiResponse.success("Budget plan submitted for approval", response));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('BUDGET_APPROVE')")
    @Operation(summary = "Approve Workflow Step", description = "Approve the active step in the workflow sequence.")
    public ResponseEntity<ApiResponse<BudgetResponse>> approveBudgetStep(
            @PathVariable Long id,
            @RequestParam(required = false) String remarks,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "anonymous";
        BudgetResponse response = budgetService.approveBudgetStep(id, remarks, username);
        return ResponseEntity.ok(ApiResponse.success("Approval step processed successfully", response));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasAuthority('BUDGET_APPROVE')")
    @Operation(summary = "Reject Budget Plan", description = "Reject the budget plan and return it to DRAFT status.")
    public ResponseEntity<ApiResponse<BudgetResponse>> rejectBudget(
            @PathVariable Long id,
            @RequestParam String remarks,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "anonymous";
        BudgetResponse response = budgetService.rejectBudget(id, remarks, username);
        return ResponseEntity.ok(ApiResponse.success("Budget plan rejected", response));
    }

    @PostMapping("/{id}/freeze")
    @PreAuthorize("hasAuthority('BUDGET_FREEZE')")
    @Operation(summary = "Freeze/Unfreeze Budget Master", description = "Freeze or unfreeze a budget plan to prevent updates.")
    public ResponseEntity<ApiResponse<BudgetResponse>> freezeBudget(
            @PathVariable Long id,
            @RequestParam Boolean isFrozen,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "anonymous";
        BudgetResponse response = budgetService.freezeBudget(id, isFrozen, username);
        return ResponseEntity.ok(ApiResponse.success("Budget plan freeze status updated to " + isFrozen, response));
    }

    @PostMapping("/lines/{lineId}/lock")
    @PreAuthorize("hasAuthority('BUDGET_LOCK')")
    @Operation(summary = "Lock/Unlock Budget Line", description = "Lock a line to block any future reservations or postings.")
    public ResponseEntity<ApiResponse<BudgetLineResponse>> lockBudgetLine(
            @PathVariable Long lineId,
            @RequestParam Boolean isLocked,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "anonymous";
        BudgetLineResponse response = budgetService.lockBudgetLine(lineId, isLocked, username);
        return ResponseEntity.ok(ApiResponse.success("Budget line lock status updated to " + isLocked, response));
    }

    @PostMapping("/revisions")
    @PreAuthorize("hasAuthority('BUDGET_REVISE')")
    @Operation(summary = "Create Revision", description = "Revise a line's allocated budget amount.")
    public ResponseEntity<ApiResponse<BudgetRevisionResponse>> createRevision(
            @RequestParam Long companyId,
            @Valid @RequestBody BudgetRevisionRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "anonymous";
        BudgetRevisionResponse response = budgetService.createRevision(companyId, request, username);
        return ResponseEntity.ok(ApiResponse.success("Budget revision approved and processed", response));
    }

    @PostMapping("/transfers")
    @PreAuthorize("hasAuthority('BUDGET_REVISE')")
    @Operation(summary = "Transfer Funds", description = "Transfer allocated funds between two budget lines.")
    public ResponseEntity<ApiResponse<BudgetRevisionResponse>> transferFunds(
            @RequestParam Long companyId,
            @RequestParam Long sourceLineId,
            @RequestParam Long targetLineId,
            @RequestParam BigDecimal amount,
            @RequestParam(required = false) String reason,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "anonymous";
        BudgetRevisionResponse response = budgetService.transferFunds(companyId, sourceLineId, targetLineId, amount, reason, username);
        return ResponseEntity.ok(ApiResponse.success("Funds transferred successfully", response));
    }

    @PostMapping("/lines/mass-update")
    @PreAuthorize("hasAuthority('BUDGET_REVISE')")
    @Operation(summary = "Bulk Adjust Allocations", description = "Mass adjust allocation amounts using percentage or fixed delta.")
    public ResponseEntity<ApiResponse<List<BudgetLineResponse>>> massUpdate(
            @RequestParam Long companyId,
            @Valid @RequestBody BudgetMassUpdateRequest request,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "anonymous";
        List<BudgetLineResponse> response = budgetService.massUpdateBudgetLines(companyId, request, username);
        return ResponseEntity.ok(ApiResponse.success("Bulk budget line updates completed", response));
    }

    @GetMapping("/compare")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "Compare Budgets", description = "Perform side-by-side variance analysis between two budgets/versions.")
    public ResponseEntity<ApiResponse<BudgetComparisonResponse>> compareBudgets(
            @RequestParam Long companyId,
            @RequestParam Long budgetId1,
            @RequestParam Long budgetId2
    ) {
        BudgetComparisonResponse response = budgetService.compareBudgets(companyId, budgetId1, budgetId2);
        return ResponseEntity.ok(ApiResponse.success("Budget comparison retrieved successfully", response));
    }

    @GetMapping("/{id}/drilldown")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "Hierarchy Drill-down Report", description = "Drill down and roll up allocations/actuals by department, cost center, project, etc.")
    public ResponseEntity<ApiResponse<List<BudgetDrilldownResponse>>> drilldownBudget(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @RequestParam String dimensionName,
            @RequestParam(required = false) String parentValue
    ) {
        List<BudgetDrilldownResponse> response = budgetService.drilldownBudget(companyId, id, dimensionName, parentValue);
        return ResponseEntity.ok(ApiResponse.success("Hierarchy drill-down retrieved", response));
    }

    @PostMapping("/{id}/copy")
    @PreAuthorize("hasAuthority('BUDGET_CREATE')")
    @Operation(summary = "Copy & Roll Forward Plan", description = "Copy a plan to another fiscal year with a percentage roll-forward multiplier.")
    public ResponseEntity<ApiResponse<BudgetResponse>> copyBudget(
            @RequestParam Long companyId,
            @PathVariable Long id,
            @RequestParam Long targetFiscalYearId,
            @RequestParam String targetCode,
            @RequestParam String targetName,
            @RequestParam(required = false) BigDecimal percentageMultiplier,
            Principal principal
    ) {
        String username = principal != null ? principal.getName() : "anonymous";
        BudgetResponse response = budgetService.copyBudget(companyId, id, targetFiscalYearId, targetCode, targetName, percentageMultiplier, username);
        return ResponseEntity.ok(ApiResponse.success("Budget plan copied and rolled forward successfully", response));
    }
}
