package com.plus33.erp.procurement.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.procurement.dto.*;
import com.plus33.erp.procurement.entity.PurchaseOrderStatus;
import com.plus33.erp.procurement.service.PurchaseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@Tag(name = "Purchase Order Management", description = "REST APIs for managing purchase orders and procurement lifecycle")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('PURCHASE_ORDER_CREATE')")
    @Operation(summary = "Create a new purchase order", description = "Initiates a purchase order in DRAFT status.")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> createPurchaseOrder(
            @Valid @RequestBody PurchaseOrderRequest request
    ) {
        PurchaseOrderResponse response = purchaseOrderService.createPurchaseOrder(request);
        return new ResponseEntity<>(ApiResponse.success("Purchase order created successfully", response), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('PURCHASE_ORDER_VIEW')")
    @Operation(summary = "Get purchase order by ID", description = "Retrieves details of a purchase order by primary key.")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> getPurchaseOrderById(@PathVariable Long id) {
        PurchaseOrderResponse response = purchaseOrderService.getPurchaseOrderById(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase order retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('PURCHASE_ORDER_VIEW')")
    @Operation(summary = "Search purchase orders", description = "Performs dynamic searches and pagination filters for purchase orders.")
    public ResponseEntity<ApiResponse<PageResponse<PurchaseOrderResponse>>> searchPurchaseOrders(
            @RequestParam(required = false) String orderNumber,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) PurchaseOrderStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expectedDeliveryDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate expectedDeliveryDateTo,
            @RequestParam(required = false) Long purchaseRequestId,
            @RequestParam(required = false) Long orderedBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        PurchaseOrderSearchRequest searchRequest = new PurchaseOrderSearchRequest(
                orderNumber, companyId, supplierId, status, expectedDeliveryDateFrom, expectedDeliveryDateTo,
                purchaseRequestId, orderedBy);
        PageResponse<PurchaseOrderResponse> response = purchaseOrderService.searchPurchaseOrders(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Purchase orders retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('PURCHASE_ORDER_UPDATE')")
    @Operation(summary = "Update purchase order details", description = "Modifies details of a purchase order while it is in DRAFT status.")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> updatePurchaseOrder(
            @PathVariable Long id,
            @Valid @RequestBody PurchaseOrderRequest request
    ) {
        PurchaseOrderResponse response = purchaseOrderService.updatePurchaseOrder(id, request);
        return ResponseEntity.ok(ApiResponse.success("Purchase order updated successfully", response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PURCHASE_ORDER_UPDATE')")
    @Operation(summary = "Delete purchase order", description = "Deletes a purchase order if it is in DRAFT status.")
    public ResponseEntity<ApiResponse<Void>> deletePurchaseOrder(@PathVariable Long id) {
        purchaseOrderService.deletePurchaseOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase order deleted successfully", null));
    }

    @PostMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('PURCHASE_ORDER_APPROVE')")
    @Operation(summary = "Approve purchase order", description = "Approves a purchase order draft and transitions it to ISSUED.")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> approvePurchaseOrder(@PathVariable Long id) {
        PurchaseOrderResponse response = purchaseOrderService.approvePurchaseOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase order approved successfully", response));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('PURCHASE_ORDER_CANCEL')")
    @Operation(summary = "Cancel purchase order", description = "Cancels an issued purchase order with a reason.")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> cancelPurchaseOrder(
            @PathVariable Long id,
            @Valid @RequestBody ReasonRequest reasonRequest
    ) {
        PurchaseOrderResponse response = purchaseOrderService.cancelPurchaseOrder(id, reasonRequest.reason());
        return ResponseEntity.ok(ApiResponse.success("Purchase order cancelled successfully", response));
    }

    @PostMapping("/{id}/close")
    @PreAuthorize("hasAuthority('PURCHASE_ORDER_CLOSE')")
    @Operation(summary = "Close purchase order", description = "Closes a fully received purchase order.")
    public ResponseEntity<ApiResponse<PurchaseOrderResponse>> closePurchaseOrder(@PathVariable Long id) {
        PurchaseOrderResponse response = purchaseOrderService.closePurchaseOrder(id);
        return ResponseEntity.ok(ApiResponse.success("Purchase order closed successfully", response));
    }
}
