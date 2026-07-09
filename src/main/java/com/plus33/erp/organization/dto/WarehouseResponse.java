/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.dto
 * File              : WarehouseResponse.java
 * Purpose           : Data Transfer Object for request/response in Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseController
 * Related Service   : WarehouseService, WarehouseServiceImpl
 * Related Repository: WarehouseRepository
 * Related Entity    : Warehouse
 * Related DTO       : WarehouseResponse
 * Related Mapper    : WarehouseMapper
 * Related DB Table  : warehouses
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseController, WarehouseService, WarehouseServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Organization Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Organization Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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