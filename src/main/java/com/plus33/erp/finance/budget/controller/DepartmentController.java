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

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
@Tag(name = "Budget Departments", description = "API endpoints for managing organizational departments.")
public class DepartmentController {

    private final DepartmentService departmentService;

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

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "Get Department", description = "Retrieve a department by its unique ID.")
    public ResponseEntity<ApiResponse<DepartmentResponse>> getDepartment(@PathVariable Long id) {
        DepartmentResponse response = departmentService.getDepartment(id);
        return ResponseEntity.ok(ApiResponse.success("Department retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "List Departments", description = "List all departments for a company.")
    public ResponseEntity<ApiResponse<List<DepartmentResponse>>> getDepartmentsByCompany(@RequestParam Long companyId) {
        List<DepartmentResponse> response = departmentService.getDepartmentsByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Departments retrieved successfully", response));
    }
}
