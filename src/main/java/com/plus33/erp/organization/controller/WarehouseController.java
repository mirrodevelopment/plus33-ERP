package com.plus33.erp.organization.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.organization.dto.WarehouseRequest;
import com.plus33.erp.organization.dto.WarehouseResponse;
import com.plus33.erp.organization.dto.WarehouseSearchRequest;
import com.plus33.erp.organization.service.WarehouseService;
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
@RequestMapping("/api/v1/warehouses")
@Tag(name = "Warehouse Management", description = "REST APIs for managing organization warehouses")
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WAREHOUSE_CREATE')")
    @Operation(summary = "Create a new warehouse", description = "Adds a new warehouse scoped to a region. Code must be unique within the region.")
    public ResponseEntity<ApiResponse<WarehouseResponse>> createWarehouse(@Valid @RequestBody WarehouseRequest request) {
        WarehouseResponse response = warehouseService.createWarehouse(request);
        return new ResponseEntity<>(ApiResponse.success("Warehouse created successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_VIEW')")
    @Operation(summary = "Get warehouse by ID", description = "Retrieves details of a warehouse by its primary key.")
    public ResponseEntity<ApiResponse<WarehouseResponse>> getWarehouseById(@PathVariable Long id) {
        WarehouseResponse response = warehouseService.getWarehouseById(id);
        return ResponseEntity.ok(ApiResponse.success("Warehouse retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('WAREHOUSE_VIEW')")
    @Operation(summary = "Search warehouses", description = "Performs dynamic searches and pagination filters for warehouses.")
    public ResponseEntity<ApiResponse<PageResponse<WarehouseResponse>>> searchWarehouses(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        WarehouseSearchRequest searchRequest = new WarehouseSearchRequest(query, companyId, regionId, active);
        PageResponse<WarehouseResponse> response = warehouseService.searchWarehouses(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Warehouses retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_UPDATE')")
    @Operation(summary = "Update warehouse details", description = "Modifies details of an active warehouse by ID.")
    public ResponseEntity<ApiResponse<WarehouseResponse>> updateWarehouse(
            @PathVariable Long id,
            @Valid @RequestBody WarehouseRequest request
    ) {
        WarehouseResponse response = warehouseService.updateWarehouse(id, request);
        return ResponseEntity.ok(ApiResponse.success("Warehouse updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('WAREHOUSE_DELETE')")
    @Operation(summary = "Soft delete warehouse", description = "Flags active field to false to soft delete a warehouse.")
    public ResponseEntity<ApiResponse<Void>> deleteWarehouse(@PathVariable Long id) {
        warehouseService.deleteWarehouse(id);
        return ResponseEntity.ok(ApiResponse.success("Warehouse deleted successfully", null));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('WAREHOUSE_UPDATE')")
    @Operation(summary = "Activate warehouse", description = "Activates an inactive warehouse.")
    public ResponseEntity<ApiResponse<WarehouseResponse>> activateWarehouse(@PathVariable Long id) {
        WarehouseResponse response = warehouseService.activateWarehouse(id);
        return ResponseEntity.ok(ApiResponse.success("Warehouse activated successfully", response));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('WAREHOUSE_UPDATE')")
    @Operation(summary = "Deactivate warehouse", description = "Deactivates a warehouse.")
    public ResponseEntity<ApiResponse<WarehouseResponse>> deactivateWarehouse(@PathVariable Long id) {
        WarehouseResponse response = warehouseService.deactivateWarehouse(id);
        return ResponseEntity.ok(ApiResponse.success("Warehouse deactivated successfully", response));
    }
}
