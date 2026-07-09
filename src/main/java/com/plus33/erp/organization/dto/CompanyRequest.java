/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.dto
 * File              : CompanyRequest.java
 * Purpose           : Data Transfer Object for request/response in Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CompanyController
 * Related Service   : CompanyService, CompanyServiceImpl
 * Related Repository: CompanyRepository
 * Related Entity    : Company
 * Related DTO       : CompanyRequest
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
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code CompanyRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Organization Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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