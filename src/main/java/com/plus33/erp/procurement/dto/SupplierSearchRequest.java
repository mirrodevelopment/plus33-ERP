package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Supplier dynamic search criteria")
public record SupplierSearchRequest(
        @Schema(description = "Fuzzy search text matching supplier code or name", example = "Dubai")
        String query,

        @Schema(description = "Filter by company ID", example = "1")
        Long companyId,

        @Schema(description = "Filter by active status", example = "true")
        Boolean active
) {}
