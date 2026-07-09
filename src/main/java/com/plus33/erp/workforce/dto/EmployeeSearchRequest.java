/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.dto
 * File              : EmployeeSearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeSearchController
 * Related Service   : EmployeeSearchService, EmployeeSearchServiceImpl
 * Related Repository: EmployeeSearchRepository
 * Related Entity    : EmployeeSearch
 * Related DTO       : EmployeeSearchRequest
 * Related Mapper    : EmployeeSearchMapper
 * Related DB Table  : employee_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EmployeeSearchController, EmployeeSearchService, EmployeeSearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Workforce Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.workforce.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Employee search filter query")
public record EmployeeSearchRequest(
        @Schema(description = "Fuzzy search text matching employee code, first name, last name, or email", example = "John")
        String query,

        @Schema(description = "Filter by company ID", example = "1")
        Long companyId,

        @Schema(description = "Filter by region ID", example = "1")
        Long regionId,

        @Schema(description = "Filter by store ID", example = "1")
        Long storeId,

        @Schema(description = "Filter by active status", example = "true")
        Boolean active,

        @Schema(description = "Filter by designation", example = "Store Manager")
        String designation,

        @Schema(description = "Filter by employment type", example = "FULL_TIME")
        String employmentType
) {}
