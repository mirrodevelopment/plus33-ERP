package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Schema(description = "Purchase Request line item request parameters")
public record PurchaseRequestItemRequest(
        @Schema(description = "Product ID", example = "1")
        @NotNull(message = "Product ID is required")
        Long productId,

        @Schema(description = "Requested quantity", example = "100.00")
        @NotNull(message = "Requested quantity is required")
        @DecimalMin(value = "0.01", message = "Requested quantity must be greater than zero")
        BigDecimal requestedQuantity,

        @Schema(description = "Unit of measure (e.g. PCS, KG)", example = "PCS")
        @Size(max = 50, message = "Unit of measure cannot exceed 50 characters")
        String unitOfMeasure,

        @Schema(description = "Line item remarks", example = "Urgent requirement")
        @Size(max = 255, message = "Remarks cannot exceed 255 characters")
        String remarks
) {}
