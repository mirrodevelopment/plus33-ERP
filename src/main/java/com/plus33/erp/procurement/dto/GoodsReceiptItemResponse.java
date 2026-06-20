package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Goods Receipt line item response details")
public record GoodsReceiptItemResponse(
        @Schema(description = "Database ID of the line item", example = "1")
        Long id,

        @Schema(description = "Product ID", example = "1")
        Long productId,

        @Schema(description = "Product SKU / Code", example = "RAW-MLK-001")
        String productCode,

        @Schema(description = "Product Name", example = "Milk")
        String productName,

        @Schema(description = "Received quantity", example = "10.00")
        BigDecimal receivedQuantity,

        @Schema(description = "Line item remarks", example = "Delivered in good condition")
        String remarks
) {}
