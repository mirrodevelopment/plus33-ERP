package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Schema(description = "Goods Receipt creation parameters")
public record GoodsReceiptRequest(
        @Schema(description = "Purchase Order ID", example = "1")
        @NotNull(message = "Purchase Order ID is required")
        Long purchaseOrderId,

        @Schema(description = "Company ID", example = "1")
        @NotNull(message = "Company ID is required")
        Long companyId,

        @Schema(description = "Destination Warehouse ID", example = "1")
        Long warehouseId,

        @Schema(description = "Destination Store ID", example = "1")
        Long storeId,

        @Schema(description = "General remarks", example = "Deliver to back warehouse")
        String remarks,

        @Schema(description = "Supplier delivery note reference", example = "DN-90812")
        String supplierDeliveryNote,

        @Schema(description = "Supplier invoice number", example = "INV-7731")
        String supplierInvoiceNumber,

        @Schema(description = "Idempotency client reference UUID", example = "e58ec7b8-e00f-47db-83b9-0c672c1d0a89")
        @NotNull(message = "Client reference ID is required")
        UUID clientReferenceId,

        @Schema(description = "List of goods receipt items")
        @NotEmpty(message = "Goods receipt must contain at least one item")
        @Valid
        List<GoodsReceiptItemRequest> items
) {}
