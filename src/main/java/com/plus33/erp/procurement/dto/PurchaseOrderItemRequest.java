package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Purchase Order line item request parameters")
public record PurchaseOrderItemRequest(
        @Schema(description = "Product ID", example = "1")
        @NotNull(message = "Product ID is required")
        Long productId,

        @Schema(description = "Ordered quantity", example = "100.00")
        @NotNull(message = "Ordered quantity is required")
        @DecimalMin(value = "0.01", message = "Ordered quantity must be greater than zero")
        BigDecimal orderedQuantity,

        @Schema(description = "Unit price of the product", example = "4.50")
        @NotNull(message = "Unit price is required")
        @DecimalMin(value = "0.00", message = "Unit price must be greater than or equal to zero")
        BigDecimal unitPrice,

        @Schema(description = "Line item remarks", example = "Monthly order")
        @Size(max = 255, message = "Remarks cannot exceed 255 characters")
        String remarks,

        @Schema(description = "Budget dimension set ID", example = "1")
        Long dimensionSetId
) {
    public PurchaseOrderItemRequest(Long productId, BigDecimal orderedQuantity, BigDecimal unitPrice, String remarks) {
        this(productId, orderedQuantity, unitPrice, remarks, null);
    }
}
