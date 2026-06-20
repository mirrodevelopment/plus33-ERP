package com.plus33.erp.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Product search dynamic query parameters")
public record ProductSearchRequest(
        @Schema(description = "Fuzzy search text matching code or name", example = "Arabica")
        String query,

        @Schema(description = "Filter by category ID", example = "1")
        Long categoryId,

        @Schema(description = "Filter by product type", example = "RAW_MATERIAL")
        String productType,

        @Schema(description = "Filter by active status", example = "true")
        Boolean active
) {}
