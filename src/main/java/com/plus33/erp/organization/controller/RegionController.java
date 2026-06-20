package com.plus33.erp.organization.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.organization.dto.RegionRequest;
import com.plus33.erp.organization.dto.RegionResponse;
import com.plus33.erp.organization.dto.RegionSearchRequest;
import com.plus33.erp.organization.service.RegionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/regions")
@Tag(name = "Region Management", description = "REST APIs for managing organization regions")
public class RegionController {

    private final RegionService regionService;

    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('REGION_CREATE')")
    @Operation(summary = "Create a new region", description = "Adds a new region scoped to a company. Code must be unique within the company.")
    public ResponseEntity<ApiResponse<RegionResponse>> createRegion(@Valid @RequestBody RegionRequest request) {
        RegionResponse response = regionService.createRegion(request);
        return new ResponseEntity<>(ApiResponse.success("Region created successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('REGION_VIEW')")
    @Operation(summary = "Get region by ID", description = "Retrieves details of a region by its primary key.")
    public ResponseEntity<ApiResponse<RegionResponse>> getRegionById(@PathVariable Long id) {
        RegionResponse response = regionService.getRegionById(id);
        return ResponseEntity.ok(ApiResponse.success("Region retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('REGION_VIEW')")
    @Operation(summary = "Search regions", description = "Performs dynamic searches and pagination filters for regions.")
    public ResponseEntity<ApiResponse<PageResponse<RegionResponse>>> searchRegions(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long companyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        RegionSearchRequest searchRequest = new RegionSearchRequest(query, companyId);
        PageResponse<RegionResponse> response = regionService.searchRegions(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Regions retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('REGION_UPDATE')")
    @Operation(summary = "Update region details", description = "Modifies details of an active region by ID.")
    public ResponseEntity<ApiResponse<RegionResponse>> updateRegion(
            @PathVariable Long id,
            @Valid @RequestBody RegionRequest request
    ) {
        RegionResponse response = regionService.updateRegion(id, request);
        return ResponseEntity.ok(ApiResponse.success("Region updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('REGION_DELETE')")
    @Operation(summary = "Delete region", description = "Performs hard-delete of a region by ID if no active stores, warehouses, or employees depend on it.")
    public ResponseEntity<ApiResponse<Void>> deleteRegion(@PathVariable Long id) {
        regionService.deleteRegion(id);
        return ResponseEntity.ok(ApiResponse.success("Region deleted successfully", null));
    }
}
