/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.dto
 * File              : RegionSearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RegionSearchController
 * Related Service   : RegionSearchService, RegionSearchServiceImpl
 * Related Repository: RegionSearchRepository
 * Related Entity    : RegionSearch
 * Related DTO       : RegionSearchRequest
 * Related Mapper    : RegionSearchMapper
 * Related DB Table  : region_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RegionSearchController, RegionSearchService, RegionSearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Organization Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Region search filter query")
public record RegionSearchRequest(
        @Schema(description = "Fuzzy search text matching region code or name", example = "Dubai")
        String query,

        @Schema(description = "Filter by company ID", example = "1")
        Long companyId
) {}
