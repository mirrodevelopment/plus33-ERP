package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Purchase Order line item response details")
public record PurchaseOrderItemResponse(
        @Schema(description = "Database ID of the line item", example = "1")
        Long id,

        @Schema(description = "Product ID", example = "1")
        Long productId,

        @Schema(description = "Product Code", example = "PRD_001")
        String productCode,

        @Schema(description = "Product Name", example = "Whole Milk")
        String productName,

        @Schema(description = "Ordered quantity", example = "100.00")
        BigDecimal orderedQuantity,

        @Schema(description = "Unit price of the product", example = "4.50")
        BigDecimal unitPrice,

        @Schema(description = "Received quantity", example = "0.00")
        BigDecimal receivedQuantity,

        @Schema(description = "Remaining quantity", example = "100.00")
        BigDecimal remainingQuantity,

        @Schema(description = "Line item remarks", example = "Monthly order")
        String remarks
) {}
