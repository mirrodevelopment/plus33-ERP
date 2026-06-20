package com.plus33.erp.procurement.dto;

import com.plus33.erp.procurement.entity.GoodsReceiptStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Schema(description = "Goods Receipt response details")
public record GoodsReceiptResponse(
        @Schema(description = "Database ID of the goods receipt", example = "1")
        Long id,

        @Schema(description = "Business-readable unique identifier", example = "GR-2026-000001")
        String receiptNumber,

        @Schema(description = "Purchase Order ID", example = "1")
        Long purchaseOrderId,

        @Schema(description = "Purchase Order Number", example = "PO-2026-000001")
        String purchaseOrderNumber,

        @Schema(description = "Company ID", example = "1")
        Long companyId,

        @Schema(description = "Company Name", example = "PLUS33 Global Corp")
        String companyName,

        @Schema(description = "Destination Warehouse ID", example = "1")
        Long warehouseId,

        @Schema(description = "Destination Warehouse Name", example = "Dubai Warehouse")
        String warehouseName,

        @Schema(description = "Destination Store ID", example = "1")
        Long storeId,

        @Schema(description = "Destination Store Name", example = "Dubai Mall Store")
        String storeName,

        @Schema(description = "User ID who received the goods", example = "1")
        Long receivedByUserId,

        @Schema(description = "Name of the user who received the goods", example = "John")
        String receivedByUserName,

        @Schema(description = "Receipt timestamp", example = "2026-06-20T14:28:30")
        LocalDateTime receivedAt,

        @Schema(description = "Receipt status", example = "COMPLETED")
        GoodsReceiptStatus status,

        @Schema(description = "General remarks", example = "Deliver to back warehouse")
        String remarks,

        @Schema(description = "Supplier delivery note reference", example = "DN-90812")
        String supplierDeliveryNote,

        @Schema(description = "Supplier invoice number", example = "INV-7731")
        String supplierInvoiceNumber,

        @Schema(description = "Idempotency client reference UUID", example = "e58ec7b8-e00f-47db-83b9-0c672c1d0a89")
        UUID clientReferenceId,

        @Schema(description = "Total quantity received", example = "10.00")
        BigDecimal totalQuantity,

        @Schema(description = "Total calculated amount based on PO unit price", example = "45.00")
        BigDecimal totalAmount,

        @Schema(description = "User ID who cancelled the receipt", example = "2")
        Long cancelledByUserId,

        @Schema(description = "Name of the user who cancelled the receipt", example = "Jane")
        String cancelledByUserName,

        @Schema(description = "Cancellation timestamp", example = "2026-06-20T15:30:35")
        LocalDateTime cancelledAt,

        @Schema(description = "Reason for cancellation", example = "Incorrect warehouse delivery")
        String cancellationReason,

        @Schema(description = "List of received items")
        List<GoodsReceiptItemResponse> items
) {}
