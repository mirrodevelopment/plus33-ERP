package com.plus33.erp.organization.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.organization.dto.StoreRequest;
import com.plus33.erp.organization.dto.StoreResponse;
import com.plus33.erp.organization.dto.StoreSearchRequest;
import com.plus33.erp.organization.service.StoreService;
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
@RequestMapping("/api/v1/stores")
@Tag(name = "Store Management", description = "REST APIs for managing organization stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('STORE_CREATE')")
    @Operation(summary = "Create a new store", description = "Adds a new store scoped to a region. Code must be unique within the company.")
    public ResponseEntity<ApiResponse<StoreResponse>> createStore(@Valid @RequestBody StoreRequest request) {
        StoreResponse response = storeService.createStore(request);
        return new ResponseEntity<>(ApiResponse.success("Store created successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('STORE_VIEW')")
    @Operation(summary = "Get store by ID", description = "Retrieves details of a store by its primary key.")
    public ResponseEntity<ApiResponse<StoreResponse>> getStoreById(@PathVariable Long id) {
        StoreResponse response = storeService.getStoreById(id);
        return ResponseEntity.ok(ApiResponse.success("Store retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('STORE_VIEW')")
    @Operation(summary = "Search stores", description = "Performs dynamic searches and pagination filters for stores.")
    public ResponseEntity<ApiResponse<PageResponse<StoreResponse>>> searchStores(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long regionId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        StoreSearchRequest searchRequest = new StoreSearchRequest(query, companyId, regionId, warehouseId, active);
        PageResponse<StoreResponse> response = storeService.searchStores(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Stores retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('STORE_UPDATE')")
    @Operation(summary = "Update store details", description = "Modifies details of an active store by ID.")
    public ResponseEntity<ApiResponse<StoreResponse>> updateStore(
            @PathVariable Long id,
            @Valid @RequestBody StoreRequest request
    ) {
        StoreResponse response = storeService.updateStore(id, request);
        return ResponseEntity.ok(ApiResponse.success("Store updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('STORE_DELETE')")
    @Operation(summary = "Soft delete store", description = "Flags active field to false to soft delete a store.")
    public ResponseEntity<ApiResponse<Void>> deleteStore(@PathVariable Long id) {
        storeService.deleteStore(id);
        return ResponseEntity.ok(ApiResponse.success("Store deleted successfully", null));
    }

    @PatchMapping("/{id}/activate")
    @PreAuthorize("hasAuthority('STORE_UPDATE')")
    @Operation(summary = "Activate store", description = "Activates an inactive store.")
    public ResponseEntity<ApiResponse<StoreResponse>> activateStore(@PathVariable Long id) {
        StoreResponse response = storeService.activateStore(id);
        return ResponseEntity.ok(ApiResponse.success("Store activated successfully", response));
    }

    @PatchMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('STORE_UPDATE')")
    @Operation(summary = "Deactivate store", description = "Deactivates a store.")
    public ResponseEntity<ApiResponse<StoreResponse>> deactivateStore(@PathVariable Long id) {
        StoreResponse response = storeService.deactivateStore(id);
        return ResponseEntity.ok(ApiResponse.success("Store deactivated successfully", response));
    }
}
