/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.dto
 * File              : SupplierSearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierSearchController
 * Related Service   : SupplierSearchService, SupplierSearchServiceImpl
 * Related Repository: SupplierSearchRepository
 * Related Entity    : SupplierSearch
 * Related DTO       : SupplierSearchRequest
 * Related Mapper    : SupplierSearchMapper
 * Related DB Table  : supplier_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierSearchController, SupplierSearchService, SupplierSearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Procurement Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Supplier dynamic search criteria")
public record SupplierSearchRequest(
        @Schema(description = "Fuzzy search text matching supplier code or name", example = "Dubai")
        String query,

        @Schema(description = "Filter by company ID", example = "1")
        Long companyId,

        @Schema(description = "Filter by active status", example = "true")
        Boolean active
) {}
