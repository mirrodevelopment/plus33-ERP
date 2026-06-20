package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Company creation/update request details")
public record CompanyRequest(
        @Schema(description = "Unique code of the company", example = "PLUS33_GLOBAL")
        @NotBlank(message = "Company code is required")
        @Size(max = 50, message = "Company code cannot exceed 50 characters")
        String code,

        @Schema(description = "Name of the company", example = "PLUS33 Global Corp")
        @NotBlank(message = "Company name is required")
        @Size(max = 150, message = "Company name cannot exceed 150 characters")
        String name,

        @Schema(description = "Legal registered name of the company", example = "PLUS33 Global Limited")
        @Size(max = 255, message = "Legal name cannot exceed 255 characters")
        String legalName,

        @Schema(description = "Country code (ISO 3166-1 alpha-2)", example = "AE")
        @Size(max = 10, message = "Country code cannot exceed 10 characters")
        String countryCode,

        @Schema(description = "Status of the company", example = "true")
        Boolean active
) {}
