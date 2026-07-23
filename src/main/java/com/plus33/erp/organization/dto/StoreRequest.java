/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.dto
 * File              : StoreRequest.java
 * Purpose           : Data Transfer Object for request/response in Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StoreController
 * Related Service   : StoreService, StoreServiceImpl
 * Related Repository: StoreRepository
 * Related Entity    : Store
 * Related DTO       : StoreRequest
 * Related Mapper    : StoreMapper
 * Related DB Table  : stores
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : StoreController, StoreService, StoreServiceImpl
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
 * <p><b>Class  :</b> {@code StoreRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Organization Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Store creation/update request details")
public record StoreRequest(
        @Schema(description = "Unique code of the store", example = "DXB_STORE_01")
        @NotBlank(message = "Store code is required")
        @Size(max = 50, message = "Store code cannot exceed 50 characters")
        String code,

        @Schema(description = "Name of the store", example = "Dubai Mall Store")
        @NotBlank(message = "Store name is required")
        @Size(max = 100, message = "Store name cannot exceed 100 characters")
        String name,

        @Schema(description = "Address of the store", example = "Financial Centre Road, Dubai")
        @Size(max = 255, message = "Address cannot exceed 255 characters")
        String address,

        @Schema(description = "Phone number", example = "+97140000001")
        @Size(max = 30, message = "Phone number cannot exceed 30 characters")
        String phone,

        @Schema(description = "Email address", example = "store.dxb@plus33.com")
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

        @Schema(description = "Default warehouse ID mapping", example = "1")
        Long warehouseId,

        @Schema(description = "Status of the store", example = "true")
        Boolean active,

        @Schema(description = "Store type", example = "COMPACT_CAFE")
        String type,

        @Schema(description = "Store Admin Name", example = "giri")
        String adminName,

        @Schema(description = "Store Admin Number", example = "EMP10245")
        String adminNumber,

        @Schema(description = "Store Admin Mobile Contact", example = "+919999999999")
        String adminMobile
) {}