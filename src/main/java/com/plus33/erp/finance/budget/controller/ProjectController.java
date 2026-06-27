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

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
@Tag(name = "Budget Projects", description = "API endpoints for managing organizational projects.")
public class ProjectController {

    private final ProjectService projectService;

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

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "Get Project", description = "Retrieve a project by its unique ID.")
    public ResponseEntity<ApiResponse<ProjectResponse>> getProject(@PathVariable Long id) {
        ProjectResponse response = projectService.getProject(id);
        return ResponseEntity.ok(ApiResponse.success("Project retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('BUDGET_VIEW')")
    @Operation(summary = "List Projects", description = "List all projects for a company.")
    public ResponseEntity<ApiResponse<List<ProjectResponse>>> getProjectsByCompany(@RequestParam Long companyId) {
        List<ProjectResponse> response = projectService.getProjectsByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Projects retrieved successfully", response));
    }
}
