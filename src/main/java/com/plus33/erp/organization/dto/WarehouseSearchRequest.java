package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Warehouse search filter query")
public record WarehouseSearchRequest(
        @Schema(description = "Fuzzy search text matching warehouse code or name", example = "Dubai")
        String query,

        @Schema(description = "Filter by company ID", example = "1")
        Long companyId,

        @Schema(description = "Filter by region ID", example = "1")
        Long regionId,

        @Schema(description = "Filter by active status", example = "true")
        Boolean active
) {}
