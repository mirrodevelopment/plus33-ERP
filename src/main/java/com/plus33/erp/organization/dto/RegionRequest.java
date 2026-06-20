package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Region creation/update request details")
public record RegionRequest(
        @Schema(description = "Unique code of the region", example = "UAE_REGION")
        @NotBlank(message = "Region code is required")
        @Size(max = 50, message = "Region code cannot exceed 50 characters")
        String code,

        @Schema(description = "Name of the region", example = "UAE Region")
        @NotBlank(message = "Region name is required")
        @Size(max = 100, message = "Region name cannot exceed 100 characters")
        String name,

        @Schema(description = "Detailed description of the region", example = "United Arab Emirates regional operations")
        String description,

        @Schema(description = "Company ID mapping", example = "1")
        @NotNull(message = "Company ID mapping is required")
        Long companyId
) {}
