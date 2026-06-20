package com.plus33.erp.workforce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Employee search filter query")
public record EmployeeSearchRequest(
        @Schema(description = "Fuzzy search text matching employee code, first name, last name, or email", example = "John")
        String query,

        @Schema(description = "Filter by company ID", example = "1")
        Long companyId,

        @Schema(description = "Filter by region ID", example = "1")
        Long regionId,

        @Schema(description = "Filter by store ID", example = "1")
        Long storeId,

        @Schema(description = "Filter by active status", example = "true")
        Boolean active,

        @Schema(description = "Filter by designation", example = "Store Manager")
        String designation,

        @Schema(description = "Filter by employment type", example = "FULL_TIME")
        String employmentType
) {}
