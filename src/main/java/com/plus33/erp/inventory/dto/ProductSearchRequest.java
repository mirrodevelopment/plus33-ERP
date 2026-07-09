/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : ProductSearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductSearchController
 * Related Service   : ProductSearchService, ProductSearchServiceImpl
 * Related Repository: ProductSearchRepository
 * Related Entity    : ProductSearch
 * Related DTO       : ProductSearchRequest
 * Related Mapper    : ProductSearchMapper
 * Related DB Table  : product_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductSearchController, ProductSearchService, ProductSearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Product search dynamic query parameters")
public record ProductSearchRequest(
        @Schema(description = "Fuzzy search text matching code or name", example = "Arabica")
        String query,

        @Schema(description = "Filter by category ID", example = "1")
        Long categoryId,

        @Schema(description = "Filter by product type", example = "RAW_MATERIAL")
        String productType,

        @Schema(description = "Filter by active status", example = "true")
        Boolean active
) {}
