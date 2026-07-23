/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.dto
 * File              : StoreResponse.java
 * Purpose           : Data Transfer Object for request/response in Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StoreController
 * Related Service   : StoreService, StoreServiceImpl
 * Related Repository: StoreRepository
 * Related Entity    : Store
 * Related DTO       : StoreResponse
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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code StoreResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Organization Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Store details response payload")
public record StoreResponse(
        @Schema(description = "Database ID of the store", example = "1")
        Long id,

        @Schema(description = "Unique code of the store", example = "DXB_STORE_01")
        String code,

        @Schema(description = "Name of the store", example = "Dubai Mall Store")
        String name,

        @Schema(description = "Address of the store", example = "Financial Centre Road, Dubai")
        String address,

        @Schema(description = "Phone number", example = "+97140000001")
        String phone,

        @Schema(description = "Email address", example = "store.dxb@plus33.com")
        String email,

        @Schema(description = "Timezone", example = "Asia/Dubai")
        String timezone,

        @Schema(description = "Opening date", example = "2026-01-01")
        LocalDate openingDate,

        @Schema(description = "Region ID mapping", example = "1")
        Long regionId,

        @Schema(description = "Region Code of the store", example = "UAE_REGION")
        String regionCode,

        @Schema(description = "Default warehouse ID mapping", example = "1")
        Long warehouseId,

        @Schema(description = "Default warehouse Code of the store", example = "DXB_WH_01")
        String warehouseCode,

        @Schema(description = "Status of the store", example = "true")
        Boolean active,

        @Schema(description = "Creation date time", example = "2026-06-20T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Last modification date time", example = "2026-06-20T12:30:00")
        LocalDateTime updatedAt,

        @Schema(description = "Total employees assigned to this store", example = "12")
        Integer employeeCount,

        @Schema(description = "Total stock valuation linked to this store", example = "25000.00")
        Double stockValue,

        @Schema(description = "Store type", example = "COMPACT_CAFE")
        String type,

        @Schema(description = "GPS Latitude", example = "48.8566")
        java.math.BigDecimal latitude,

        @Schema(description = "GPS Longitude", example = "2.3522")
        java.math.BigDecimal longitude,

        @Schema(description = "Geofence radius in meters", example = "200")
        Integer geofenceRadiusMeters,

        @Schema(description = "Nation / Country Code of the store", example = "FR")
        String countryCode,

        @Schema(description = "Attached store compliance and licensing documents")
        java.util.List<StoreDocumentResponse> documents,

        @Schema(description = "Name of the Store Admin / Manager", example = "giri")
        String adminName,

        @Schema(description = "Employee number of the Store Admin", example = "EMP10245")
        String adminNumber,

        @Schema(description = "Mobile phone of the Store Admin", example = "+919999999999")
        String adminMobile
) {}