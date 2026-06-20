package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

@Schema(description = "Purchase Request line item response details")
public record PurchaseRequestItemResponse(
        @Schema(description = "Database ID of the line item", example = "1")
        Long id,

        @Schema(description = "Product ID", example = "1")
        Long productId,

        @Schema(description = "Product Code", example = "PRD_001")
        String productCode,

        @Schema(description = "Product Name", example = "Whole Milk")
        String productName,

        @Schema(description = "Requested quantity", example = "100.00")
        BigDecimal requestedQuantity,

        @Schema(description = "Approved quantity", example = "100.00")
        BigDecimal approvedQuantity,

        @Schema(description = "Unit of measure", example = "PCS")
        String unitOfMeasure,

        @Schema(description = "Line item remarks", example = "Urgent requirement")
        String remarks
) {}
