package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Region details response payload")
public record RegionResponse(
        @Schema(description = "Database ID of the region", example = "1")
        Long id,

        @Schema(description = "Unique code of the region", example = "UAE_REGION")
        String code,

        @Schema(description = "Name of the region", example = "UAE Region")
        String name,

        @Schema(description = "Detailed description of the region", example = "United Arab Emirates regional operations")
        String description,

        @Schema(description = "Company ID mapping", example = "1")
        Long companyId,

        @Schema(description = "Company Code of the region", example = "PLUS33_GLOBAL")
        String companyCode,

        @Schema(description = "Creation date time", example = "2026-06-20T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Last modification date time", example = "2026-06-20T12:30:00")
        LocalDateTime updatedAt
) {}
