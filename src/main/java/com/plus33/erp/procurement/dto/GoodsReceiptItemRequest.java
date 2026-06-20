package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Goods Receipt line item request parameters")
public record GoodsReceiptItemRequest(
        @Schema(description = "Product ID", example = "1")
        @NotNull(message = "Product ID is required")
        Long productId,

        @Schema(description = "Received quantity", example = "10.00")
        @NotNull(message = "Received quantity is required")
        @DecimalMin(value = "0.01", message = "Received quantity must be greater than zero")
        BigDecimal receivedQuantity,

        @Schema(description = "Line item remarks", example = "Delivered in good condition")
        @Size(max = 255, message = "Remarks cannot exceed 255 characters")
        String remarks
) {}
