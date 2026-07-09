/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.controller
 * File              : ProductionOrderController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionOrderController
 * Related Service   : ProductionOrderControllerService, ProductionOrderControllerServiceImpl
 * Related Repository: ProductionOrderControllerRepository
 * Related Entity    : ProductionOrderController
 * Related DTO       : ApiResponse, CompleteOperationRequest, CreateProductionOrderRequest, ProductionOrderDto, ProductionOrderOperationDto
 * Related Mapper    : ProductionOrderControllerMapper
 * Related DB Table  : production_order_controllers
 * Related REST APIs : POST /api/v1/manufacturing/production-orders, GET /api/v1/manufacturing/production-orders/{id}, GET /api/v1/manufacturing/production-orders/company/{companyId}, PUT /api/v1/manufacturing/production-orders/{id}/release
 * Depends On        : Common Module
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Manufacturing Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/manufacturing/production-orders, GET /api/v1/manufacturing/production-orders/{id}, GET /api/v1/manufacturing/production-orders/company/{companyId}, PUT /api/v1/manufacturing/production-orders/{id}/release
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ProductionOrderController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to ProductionOrderService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> ProductionOrderController.endpoint()
 *   --> ProductionOrderService.method()
 *   --> ProductionOrderRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/manufacturing/production-orders, GET /api/v1/manufacturing/production-orders/{id}, GET /api/v1/manufacturing/production-orders/company/{companyId}, PUT /api/v1/manufacturing/production-orders/{id}/release, PUT /api/v1/manufacturing/production-orders/{id}/operations/{operationId}/complete</p>
 * <p><b>Module Deps      :</b> Common, Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/manufacturing/production-orders")
@Tag(name = "Production Order Management", description = "REST APIs for managing shop floor production orders and operations execution")
public class ProductionOrderController {

    private final ProductionOrderService productionOrderService;

    public ProductionOrderController(ProductionOrderService productionOrderService) {
        this.productionOrderService = productionOrderService;
    }

    /**
     * Creates a new production order and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param request the validated request DTO containing input data
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping
    @PreAuthorize("hasAuthority('MANUFACTURING_MANAGE')")
    @Operation(summary = "Create Production Order", description = "Creates a shop floor production order based on product BOM and routing")
    public ResponseEntity<ApiResponse<ProductionOrderDto>> createProductionOrder(@Valid @RequestBody CreateProductionOrderRequest request) {
        ProductionOrderDto dto = productionOrderService.createProductionOrder(request);
        return new ResponseEntity<>(ApiResponse.success("Production order created successfully", dto), HttpStatus.CREATED);
    }

    /**
     * Retrieves a single production order by id by its identifier.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get Production Order by ID", description = "Retrieves production order details and operation statuses")
    public ResponseEntity<ApiResponse<ProductionOrderDto>> getProductionOrderById(@PathVariable Long id) {
        ProductionOrderDto dto = productionOrderService.getProductionOrderById(id);
        return ResponseEntity.ok(ApiResponse.success("Production order retrieved successfully", dto));
    }

    /**
     * Retrieves production orders by company data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasAuthority('MANUFACTURING_VIEW')")
    @Operation(summary = "Get Production Orders by Company", description = "Retrieves all production orders for a given company")
    public ResponseEntity<ApiResponse<List<ProductionOrderDto>>> getProductionOrdersByCompany(@PathVariable Long companyId) {
        List<ProductionOrderDto> dtoList = productionOrderService.getProductionOrdersByCompany(companyId);
        return ResponseEntity.ok(ApiResponse.success("Production orders retrieved successfully", dtoList));
    }

    /**
     * Releases previously reserved production order resources back to the available pool.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PutMapping("/{id}/release")
    @PreAuthorize("hasAuthority('MANUFACTURING_MANAGE')")
    @Operation(summary = "Release Production Order", description = "Releases production order to plant floor for execution and reserves inventory components")
    public ResponseEntity<ApiResponse<ProductionOrderDto>> releaseProductionOrder(@PathVariable Long id) {
        ProductionOrderDto dto = productionOrderService.releaseProductionOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Production order released successfully", dto));
    }

    /**
     * Completes the operation workflow and finalizes the record status.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
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

    /**
     * Completes the production order workflow and finalizes the record status.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param id the unique database ID of the resource
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAuthority('MANUFACTURING_MANAGE')")
    @Operation(summary = "Complete Production Order", description = "Finalizes production order, posts manufacturing variance journal entries, and receives finished goods into inventory")
    public ResponseEntity<ApiResponse<ProductionOrderDto>> completeProductionOrder(@PathVariable Long id) {
        ProductionOrderDto dto = productionOrderService.completeProductionOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Production order completed successfully", dto));
    }
}