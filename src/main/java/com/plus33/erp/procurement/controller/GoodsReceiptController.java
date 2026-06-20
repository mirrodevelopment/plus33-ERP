package com.plus33.erp.procurement.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.common.dto.IdempotentCreateResult;
import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.procurement.dto.*;
import com.plus33.erp.procurement.entity.GoodsReceiptStatus;
import com.plus33.erp.procurement.service.GoodsReceiptService;
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
@RequestMapping("/api/v1/goods-receipts")
@Tag(name = "Goods Receipt Management", description = "REST APIs for managing goods receipts and warehouse stock entry")
public class GoodsReceiptController {

    private final GoodsReceiptService goodsReceiptService;

    public GoodsReceiptController(GoodsReceiptService goodsReceiptService) {
        this.goodsReceiptService = goodsReceiptService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('GOODS_RECEIPT_CREATE')")
    @Operation(summary = "Record a new goods receipt", description = "Receives products against a purchase order and increases inventory stock.")
    public ResponseEntity<ApiResponse<GoodsReceiptResponse>> createGoodsReceipt(
            @Valid @RequestBody GoodsReceiptRequest request
    ) {
        IdempotentCreateResult<GoodsReceiptResponse> result = goodsReceiptService.createGoodsReceipt(request);
        if (result.created()) {
            return new ResponseEntity<>(ApiResponse.success("Goods receipt recorded successfully", result.data()), HttpStatus.CREATED);
        } else {
            return ResponseEntity.ok(ApiResponse.success("Goods receipt retrieved (idempotent request)", result.data()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('GOODS_RECEIPT_VIEW')")
    @Operation(summary = "Get goods receipt by ID", description = "Retrieves details of a goods receipt by primary key.")
    public ResponseEntity<ApiResponse<GoodsReceiptResponse>> getGoodsReceiptById(@PathVariable Long id) {
        GoodsReceiptResponse response = goodsReceiptService.getGoodsReceiptById(id);
        return ResponseEntity.ok(ApiResponse.success("Goods receipt retrieved successfully", response));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('GOODS_RECEIPT_VIEW')")
    @Operation(summary = "Search goods receipts", description = "Performs dynamic searches and pagination filters for goods receipts.")
    public ResponseEntity<ApiResponse<PageResponse<GoodsReceiptResponse>>> searchGoodsReceipts(
            @RequestParam(required = false) String receiptNumber,
            @RequestParam(required = false) Long companyId,
            @RequestParam(required = false) Long purchaseOrderId,
            @RequestParam(required = false) Long warehouseId,
            @RequestParam(required = false) Long storeId,
            @RequestParam(required = false) GoodsReceiptStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate receivedAtFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate receivedAtTo,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        Sort.Direction dir = Sort.Direction.fromString(direction);
        Pageable pageable = PageRequest.of(page, size, Sort.by(dir, sort));
        GoodsReceiptSearchRequest searchRequest = new GoodsReceiptSearchRequest(
                receiptNumber, companyId, purchaseOrderId, warehouseId, storeId, status, receivedAtFrom, receivedAtTo);
        PageResponse<GoodsReceiptResponse> response = goodsReceiptService.searchGoodsReceipts(searchRequest, pageable);
        return ResponseEntity.ok(ApiResponse.success("Goods receipts retrieved successfully", response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('GOODS_RECEIPT_UPDATE')")
    @Operation(summary = "Update goods receipt remarks/references", description = "Allows updating remarks or supplier delivery references only.")
    public ResponseEntity<ApiResponse<GoodsReceiptResponse>> updateGoodsReceipt(
            @PathVariable Long id,
            @Valid @RequestBody GoodsReceiptUpdateRequest request
    ) {
        GoodsReceiptResponse response = goodsReceiptService.updateGoodsReceipt(id, request);
        return ResponseEntity.ok(ApiResponse.success("Goods receipt updated successfully", response));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAuthority('GOODS_RECEIPT_CANCEL')")
    @Operation(summary = "Cancel completed goods receipt", description = "Cancels a goods receipt and reverses its inventory stock changes.")
    public ResponseEntity<ApiResponse<GoodsReceiptResponse>> cancelGoodsReceipt(
            @PathVariable Long id,
            @Valid @RequestBody ReasonRequest reasonRequest
    ) {
        GoodsReceiptResponse response = goodsReceiptService.cancelGoodsReceipt(id, reasonRequest.reason());
        return ResponseEntity.ok(ApiResponse.success("Goods receipt cancelled successfully", response));
    }
}
