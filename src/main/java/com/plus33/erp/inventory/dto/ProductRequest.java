package com.plus33.erp.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

@Schema(description = "Product creation/update request details")
public record ProductRequest(
        @Schema(description = "Unique SKU / Code of the product", example = "RAW-ARA-001")
        @NotBlank(message = "Product code is required")
        @Size(max = 100, message = "Product code cannot exceed 100 characters")
        String code,

        @Schema(description = "Name of the product", example = "Arabica Beans")
        @NotBlank(message = "Product name is required")
        @Size(max = 150, message = "Product name cannot exceed 150 characters")
        String name,

        @Schema(description = "Category ID mapping", example = "1")
        @NotNull(message = "Category ID is required")
        Long categoryId,

        @Schema(description = "Unit of measure ID mapping", example = "1")
        @NotNull(message = "Unit of measure ID is required")
        Long unitId,

        @Schema(description = "Type of product (e.g. RAW_MATERIAL, FINISHED_GOOD)", example = "RAW_MATERIAL")
        @NotBlank(message = "Product type is required")
        @Size(max = 50, message = "Product type cannot exceed 50 characters")
        String productType,

        @Schema(description = "Reorder limit level", example = "10.00")
        BigDecimal reorderLevel,

        @Schema(description = "Status of the product", example = "true")
        Boolean active
) {}
