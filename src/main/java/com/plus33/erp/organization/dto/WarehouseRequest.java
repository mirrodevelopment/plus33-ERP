/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.dto
 * File              : WarehouseRequest.java
 * Purpose           : Data Transfer Object for request/response in Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseController
 * Related Service   : WarehouseService, WarehouseServiceImpl
 * Related Repository: WarehouseRepository
 * Related Entity    : Warehouse
 * Related DTO       : WarehouseRequest
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Organization Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Warehouse creation/update request details")
public record WarehouseRequest(
        @Schema(description = "Unique code of the warehouse", example = "DXB_WH_01")
        @NotBlank(message = "Warehouse code is required")
        @Size(max = 50, message = "Warehouse code cannot exceed 50 characters")
        String code,

        @Schema(description = "Name of the warehouse", example = "Dubai Central Warehouse")
        @NotBlank(message = "Warehouse name is required")
        @Size(max = 100, message = "Warehouse name cannot exceed 100 characters")
        String name,

        @Schema(description = "Address of the warehouse", example = "Al Quoz Industrial Area, Dubai")
        @Size(max = 255, message = "Address cannot exceed 255 characters")
        String address,

        @Schema(description = "Phone number", example = "+97140000000")
        @Size(max = 30, message = "Phone number cannot exceed 30 characters")
        String phone,

        @Schema(description = "Email address", example = "wh.dxb@plus33.com")
        @Size(max = 150, message = "Email cannot exceed 150 characters")
        String email,

        @Schema(description = "Timezone", example = "Asia/Dubai")
        @Size(max = 100, message = "Timezone cannot exceed 100 characters")
        String timezone,

        @Schema(description = "Opening date", example = "2026-01-01")
        LocalDate openingDate,

        @Schema(description = "Region ID mapping", example = "1")
        @NotNull(message = "Region ID is required")
        Long regionId,

        @Schema(description = "Status of the warehouse", example = "true")
        Boolean active
) {}