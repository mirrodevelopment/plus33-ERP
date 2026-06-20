package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Region search filter query")
public record RegionSearchRequest(
        @Schema(description = "Fuzzy search text matching region code or name", example = "Dubai")
        String query,

        @Schema(description = "Filter by company ID", example = "1")
        Long companyId
) {}
