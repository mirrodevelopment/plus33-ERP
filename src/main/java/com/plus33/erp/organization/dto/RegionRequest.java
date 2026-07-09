/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.dto
 * File              : RegionRequest.java
 * Purpose           : Data Transfer Object for request/response in Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RegionController
 * Related Service   : RegionService, RegionServiceImpl
 * Related Repository: RegionRepository
 * Related Entity    : Region
 * Related DTO       : RegionRequest
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code RegionRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Organization Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
        Long companyId,

        @Schema(description = "Parent Region ID mapping (Country mapping)", example = "7")
        Long parentId
) {
    public RegionRequest(String code, String name, String description, Long companyId) {
        this(code, name, description, companyId, null);
    }
}