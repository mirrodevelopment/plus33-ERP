/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.dto
 * File              : StoreSearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StoreSearchController
 * Related Service   : StoreSearchService, StoreSearchServiceImpl
 * Related Repository: StoreSearchRepository
 * Related Entity    : StoreSearch
 * Related DTO       : StoreSearchRequest
 * Related Mapper    : StoreSearchMapper
 * Related DB Table  : store_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : StoreSearchController, StoreSearchService, StoreSearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Organization Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Store search filter query")
public record StoreSearchRequest(
        @Schema(description = "Fuzzy search text matching store code or name", example = "Dubai")
        String query,

        @Schema(description = "Filter by company ID", example = "1")
        Long companyId,

        @Schema(description = "Filter by region ID", example = "1")
        Long regionId,

        @Schema(description = "Filter by default warehouse ID", example = "1")
        Long warehouseId,

        @Schema(description = "Filter by active status", example = "true")
        Boolean active
) {}
