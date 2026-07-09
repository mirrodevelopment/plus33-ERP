/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.dto
 * File              : CompanySearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CompanySearchController
 * Related Service   : CompanySearchService, CompanySearchServiceImpl
 * Related Repository: CompanySearchRepository
 * Related Entity    : CompanySearch
 * Related DTO       : CompanySearchRequest
 * Related Mapper    : CompanySearchMapper
 * Related DB Table  : company_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CompanySearchController, CompanySearchService, CompanySearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Organization Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Company search filter query")
public record CompanySearchRequest(
        @Schema(description = "Fuzzy search text matching company code, name, or legal name", example = "Global")
        String query,

        @Schema(description = "Filter by active status", example = "true")
        Boolean active
) {}
