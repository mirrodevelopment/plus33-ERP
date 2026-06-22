package com.plus33.erp.inventory.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.inventory.dto.*;
import com.plus33.erp.inventory.service.InventoryTraceabilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Inventory Traceability", description = "REST APIs for managing inventory lots, serials, recalls, and traces")
public class InventoryTraceabilityController {

    private final InventoryTraceabilityService service;

    public InventoryTraceabilityController(InventoryTraceabilityService service) {
        this.service = service;
    }

    @PostMapping("/inventory-lots")
    @PreAuthorize("hasAuthority('INVENTORY_LOT_CREATE')")
    @Operation(summary = "Create a new inventory lot", description = "Defines a new product batch/lot with expiry tracking")
    public ResponseEntity<ApiResponse<InventoryLotResponse>> createLot(
            @Valid @RequestBody InventoryLotRequest request
    ) {
        InventoryLotResponse response = service.createLot(request);
        return new ResponseEntity<>(ApiResponse.success("Inventory lot created successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/inventory-lots")
    @PreAuthorize("hasAuthority('INVENTORY_LOT_VIEW')")
    @Operation(summary = "List all inventory lots")
    public ResponseEntity<ApiResponse<List<InventoryLotResponse>>> getAllLots() {
        List<InventoryLotResponse> response = service.getAllLots();
        return ResponseEntity.ok(ApiResponse.success("Inventory lots retrieved successfully", response));
    }

    @GetMapping("/inventory-lots/{id}")
    @PreAuthorize("hasAuthority('INVENTORY_LOT_VIEW')")
    @Operation(summary = "Get single inventory lot by ID")
    public ResponseEntity<ApiResponse<InventoryLotResponse>> getLotById(@PathVariable Long id) {
        InventoryLotResponse response = service.getLotById(id);
        return ResponseEntity.ok(ApiResponse.success("Inventory lot retrieved successfully", response));
    }

    @GetMapping("/inventory-serials")
    @PreAuthorize("hasAuthority('INVENTORY_SERIAL_VIEW')")
    @Operation(summary = "List all serialized items")
    public ResponseEntity<ApiResponse<List<InventorySerialResponse>>> getAllSerials() {
        List<InventorySerialResponse> response = service.getAllSerials();
        return ResponseEntity.ok(ApiResponse.success("Inventory serials retrieved successfully", response));
    }

    @GetMapping("/inventory-trace/{productId}")
    @PreAuthorize("hasAuthority('INVENTORY_TRACE_VIEW')")
    @Operation(summary = "Trace history for product", description = "Returns end-to-end genealogy trace ledger for a product")
    public ResponseEntity<ApiResponse<List<InventoryTraceEventResponse>>> getProductTrace(@PathVariable Long productId) {
        List<InventoryTraceEventResponse> response = service.getProductTrace(productId);
        return ResponseEntity.ok(ApiResponse.success("Product trace history retrieved successfully", response));
    }

    @GetMapping("/inventory-trace/lot/{lotNumber}")
    @PreAuthorize("hasAuthority('INVENTORY_TRACE_VIEW')")
    @Operation(summary = "Trace history by lot number")
    public ResponseEntity<ApiResponse<List<InventoryTraceEventResponse>>> getLotTrace(@PathVariable String lotNumber) {
        List<InventoryTraceEventResponse> response = service.getLotTrace(lotNumber);
        return ResponseEntity.ok(ApiResponse.success("Lot trace history retrieved successfully", response));
    }

    @GetMapping("/inventory-trace/serial/{serialNumber}")
    @PreAuthorize("hasAuthority('INVENTORY_TRACE_VIEW')")
    @Operation(summary = "Trace history by serial number")
    public ResponseEntity<ApiResponse<List<InventoryTraceEventResponse>>> getSerialTrace(@PathVariable String serialNumber) {
        List<InventoryTraceEventResponse> response = service.getSerialTrace(serialNumber);
        return ResponseEntity.ok(ApiResponse.success("Serial trace history retrieved successfully", response));
    }

    @PostMapping("/inventory-recalls")
    @PreAuthorize("hasAuthority('INVENTORY_RECALL_CREATE')")
    @Operation(summary = "Initiate a product recall", description = "Quarantines/recalls target lot and its serials, logging auditing details")
    public ResponseEntity<ApiResponse<InventoryRecallResponse>> createRecall(
            @Valid @RequestBody InventoryRecallRequest request
    ) {
        InventoryRecallResponse response = service.createRecall(request);
        return new ResponseEntity<>(ApiResponse.success("Inventory recall initiated successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/inventory-recalls")
    @PreAuthorize("hasAuthority('INVENTORY_RECALL_VIEW')")
    @Operation(summary = "List all inventory recalls")
    public ResponseEntity<ApiResponse<List<InventoryRecallResponse>>> getAllRecalls() {
        List<InventoryRecallResponse> response = service.getAllRecalls();
        return ResponseEntity.ok(ApiResponse.success("Inventory recalls retrieved successfully", response));
    }
}
