package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Company details response payload")
public record CompanyResponse(
        @Schema(description = "Database ID of the company", example = "1")
        Long id,

        @Schema(description = "Unique code of the company", example = "PLUS33_GLOBAL")
        String code,

        @Schema(description = "Name of the company", example = "PLUS33 Global Corp")
        String name,

        @Schema(description = "Legal registered name of the company", example = "PLUS33 Global Limited")
        String legalName,

        @Schema(description = "Country code (ISO 3166-1 alpha-2)", example = "AE")
        String countryCode,

        @Schema(description = "Status of the company", example = "true")
        Boolean active,

        @Schema(description = "Creation date time", example = "2026-06-20T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Last modification date time", example = "2026-06-20T12:30:00")
        LocalDateTime updatedAt
) {}
