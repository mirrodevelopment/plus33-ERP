package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Company search filter query")
public record CompanySearchRequest(
        @Schema(description = "Fuzzy search text matching company code, name, or legal name", example = "Global")
        String query,

        @Schema(description = "Filter by active status", example = "true")
        Boolean active
) {}
