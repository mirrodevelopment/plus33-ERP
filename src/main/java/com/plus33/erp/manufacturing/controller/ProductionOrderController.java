package com.plus33.erp.manufacturing.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.manufacturing.dto.CompleteOperationRequest;
import com.plus33.erp.manufacturing.dto.CreateProductionOrderRequest;
import com.plus33.erp.manufacturing.dto.ProductionOrderDto;
import com.plus33.erp.manufacturing.dto.ProductionOrderOperationDto;
import com.plus33.erp.manufacturing.service.ProductionOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manufacturing/production-orders")
@Tag(name = "Production Order Management", description = "REST APIs for managing shop floor production orders and operations execution")
public class ProductionOrderController {

    private final ProductionOrderService productionOrderService;

    public ProductionOrderController(ProductionOrderService productionOrderService) {
        this.productionOrderService = productionOrderService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('MANUFACTURING_MANAGE')")
    @Operation(summary = "Create Production Order", description = "Creates a shop floor production order based on product BOM and routing")
    public ResponseEntity<ApiResponse<ProductionOrderDto>> createProductionOrder(@Valid @RequestBody CreateProductionOrderRequest request) {
        ProductionOrderDto dto = productionOrderService.createProductionOrder(request);
        return new ResponseEntity<>(ApiResponse.success("Production order created successfully", dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get Production Order by ID", description = "Retrieves production order details and operation statuses")
    public ResponseEntity<ApiResponse<ProductionOrderDto>> getProductionOrderById(@PathVariable Long id) {
        ProductionOrderDto dto = productionOrderService.getProductionOrderById(id);
        return ResponseEntity.ok(ApiResponse.success("Production order retrieved successfully", dto));
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get Production Orders by Company", description = "Retrieves all production orders for a given company")
    public ResponseEntity<ApiResponse<List<ProductionOrderDto>>> getProductionOrdersByCompany(@PathVariable Long companyId) {
        List<ProductionOrderDto> dtoList = productionOrderService.getProductionOrdersByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Production orders retrieved successfully", dtoList));
    }

    @PutMapping("/{id}/release")
    @PreAuthorize("hasAuthority('MANUFACTURING_MANAGE')")
    @Operation(summary = "Release Production Order", description = "Releases production order to plant floor for execution and reserves inventory components")
    public ResponseEntity<ApiResponse<ProductionOrderDto>> releaseProductionOrder(@PathVariable Long id) {
        ProductionOrderDto dto = productionOrderService.releaseProductionOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Production order released successfully", dto));
    }

    @PutMapping("/{id}/operations/{operationId}/complete")
    @PreAuthorize("hasAuthority('MANUFACTURING_MANAGE')")
    @Operation(summary = "Complete Shop Floor Operation", description = "Records actual execution time, yields, and scrap for an operation")
    public ResponseEntity<ApiResponse<ProductionOrderOperationDto>> completeOperation(
            @PathVariable Long id,
            @PathVariable Long operationId,
            @Valid @RequestBody CompleteOperationRequest request) {
        ProductionOrderOperationDto dto = productionOrderService.completeOperation(id, operationId, request);
        return ResponseEntity.ok(ApiResponse.success("Operation completed successfully", dto));
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAuthority('MANUFACTURING_MANAGE')")
    @Operation(summary = "Complete Production Order", description = "Finalizes production order, posts manufacturing variance journal entries, and receives finished goods into inventory")
    public ResponseEntity<ApiResponse<ProductionOrderDto>> completeProductionOrder(@PathVariable Long id) {
        ProductionOrderDto dto = productionOrderService.completeProductionOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Production order completed successfully", dto));
    }
}
