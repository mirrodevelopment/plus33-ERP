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

@RestController
@RequestMapping("/api/v1/cost-centers")
@RequiredArgsConstructor
@Tag(name = "Budget Cost Centers", description = "API endpoints for managing cost centers.")
public class CostCenterController {

    private final CostCenterService costCenterService;

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

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "Get Cost Center", description = "Retrieve a cost center by its unique ID.")
    public ResponseEntity<ApiResponse<CostCenterResponse>> getCostCenter(@PathVariable Long id) {
        CostCenterResponse response = costCenterService.getCostCenter(id);
        return ResponseEntity.ok(ApiResponse.success("Cost center retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "List Cost Centers", description = "List all cost centers for a company.")
    public ResponseEntity<ApiResponse<List<CostCenterResponse>>> getCostCentersByCompany(@RequestParam Long companyId) {
        List<CostCenterResponse> response = costCenterService.getCostCentersByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Cost centers retrieved successfully", response));
    }
}
