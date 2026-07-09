/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.dto
 * File              : CompanyResponse.java
 * Purpose           : Data Transfer Object for request/response in Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CompanyController
 * Related Service   : CompanyService, CompanyServiceImpl
 * Related Repository: CompanyRepository
 * Related Entity    : Company
 * Related DTO       : CompanyResponse
 * Related Mapper    : CompanyMapper
 * Related DB Table  : companys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CompanyController, CompanyService, CompanyServiceImpl
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
 * <p><b>Class  :</b> {@code CompanyResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Organization Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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