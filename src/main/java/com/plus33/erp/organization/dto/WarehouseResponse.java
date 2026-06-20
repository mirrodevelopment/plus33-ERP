package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Warehouse details response payload")
public record WarehouseResponse(
        @Schema(description = "Database ID of the warehouse", example = "1")
        Long id,

        @Schema(description = "Unique code of the warehouse", example = "DXB_WH_01")
        String code,

        @Schema(description = "Name of the warehouse", example = "Dubai Central Warehouse")
        String name,

        @Schema(description = "Address of the warehouse", example = "Al Quoz Industrial Area, Dubai")
        String address,

        @Schema(description = "Phone number", example = "+97140000000")
        String phone,

        @Schema(description = "Email address", example = "wh.dxb@plus33.com")
        String email,

        @Schema(description = "Timezone", example = "Asia/Dubai")
        String timezone,

        @Schema(description = "Opening date", example = "2026-01-01")
        LocalDate openingDate,

        @Schema(description = "Region ID mapping", example = "1")
        Long regionId,

        @Schema(description = "Region Code of the warehouse", example = "UAE_REGION")
        String regionCode,

        @Schema(description = "Status of the warehouse", example = "true")
        Boolean active,

        @Schema(description = "Creation date time", example = "2026-06-20T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Last modification date time", example = "2026-06-20T12:30:00")
        LocalDateTime updatedAt
) {}
