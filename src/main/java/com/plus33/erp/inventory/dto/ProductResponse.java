package com.plus33.erp.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Product details response payload")
public record ProductResponse(
        @Schema(description = "Database ID of the product", example = "1")
        Long id,

        @Schema(description = "SKU / Code of the product", example = "RAW-ARA-001")
        String code,

        @Schema(description = "Name of the product", example = "Arabica Beans")
        String name,

        @Schema(description = "Category ID of the product", example = "1")
        Long categoryId,

        @Schema(description = "Category Name of the product", example = "Coffee Beans")
        String categoryName,

        @Schema(description = "Unit of measure ID of the product", example = "1")
        Long unitId,

        @Schema(description = "Unit of measure Code of the product", example = "KG")
        String unitCode,

        @Schema(description = "Type of product", example = "RAW_MATERIAL")
        String productType,

        @Schema(description = "Reorder limit level", example = "10.00")
        BigDecimal reorderLevel,

        @Schema(description = "Status of the product", example = "true")
        Boolean active,

        @Schema(description = "Creation date time", example = "2026-06-20T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Last modification date time", example = "2026-06-20T12:30:00")
        LocalDateTime updatedAt
) {}
