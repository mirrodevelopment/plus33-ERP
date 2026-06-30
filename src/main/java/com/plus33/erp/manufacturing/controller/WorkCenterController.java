package com.plus33.erp.manufacturing.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.manufacturing.dto.CreateWorkCenterRequest;
import com.plus33.erp.manufacturing.dto.WorkCenterDto;
import com.plus33.erp.manufacturing.service.WorkCenterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manufacturing/work-centers")
@Tag(name = "Work Center Management", description = "REST APIs for managing manufacturing plant work centers and capacities")
public class WorkCenterController {

    private final WorkCenterService workCenterService;

    public WorkCenterController(WorkCenterService workCenterService) {
        this.workCenterService = workCenterService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANUFACTURING_MANAGE')")
    @Operation(summary = "Create Work Center", description = "Creates a new plant work center with hourly rates and capacity")
    public ResponseEntity<ApiResponse<WorkCenterDto>> createWorkCenter(@Valid @RequestBody CreateWorkCenterRequest request) {
        WorkCenterDto dto = workCenterService.createWorkCenter(request);
        return new ResponseEntity<>(ApiResponse.success("Work center created successfully", dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get Work Center by ID", description = "Retrieves work center details by ID")
    public ResponseEntity<ApiResponse<WorkCenterDto>> getWorkCenterById(@PathVariable Long id) {
        WorkCenterDto dto = workCenterService.getWorkCenterById(id);
        return ResponseEntity.ok(ApiResponse.success("Work center retrieved successfully", dto));
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get Work Centers by Company", description = "Retrieves all work centers for a given company")
    public ResponseEntity<ApiResponse<List<WorkCenterDto>>> getWorkCentersByCompany(@PathVariable Long companyId) {
        List<WorkCenterDto> dtoList = workCenterService.getWorkCentersByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Work centers retrieved successfully", dtoList));
    }
}
