/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.dto
 * File              : WarehouseSearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Organization Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseSearchController
 * Related Service   : WarehouseSearchService, WarehouseSearchServiceImpl
 * Related Repository: WarehouseSearchRepository
 * Related Entity    : WarehouseSearch
 * Related DTO       : WarehouseSearchRequest
 * Related Mapper    : WarehouseSearchMapper
 * Related DB Table  : warehouse_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseSearchController, WarehouseSearchService, WarehouseSearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Organization Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Warehouse search filter query")
public record WarehouseSearchRequest(
        @Schema(description = "Fuzzy search text matching warehouse code or name", example = "Dubai")
        String query,

        @Schema(description = "Filter by company ID", example = "1")
        Long companyId,

        @Schema(description = "Filter by region ID", example = "1")
        Long regionId,

        @Schema(description = "Filter by active status", example = "true")
        Boolean active
) {}
