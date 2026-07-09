/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.dto
 * File              : RegionResponse.java
 * Purpose           : Data Transfer Object for request/response in Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RegionController
 * Related Service   : RegionService, RegionServiceImpl
 * Related Repository: RegionRepository
 * Related Entity    : Region
 * Related DTO       : RegionResponse
 * Related Mapper    : RegionMapper
 * Related DB Table  : regions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RegionController, RegionService, RegionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Organization Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code RegionResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Organization Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

        @Schema(description = "Parent Region ID (Country mapping)", example = "7")
        Long parentId,

        @Schema(description = "Parent Region Name (Country name)", example = "France")
        String parentName,

        @Schema(description = "Creation date time", example = "2026-06-20T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Last modification date time", example = "2026-06-20T12:30:00")
        LocalDateTime updatedAt
) {}