/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : CustomerSearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerSearchController
 * Related Service   : CustomerSearchService, CustomerSearchServiceImpl
 * Related Repository: CustomerSearchRepository
 * Related Entity    : CustomerSearch
 * Related DTO       : CustomerSearchRequest
 * Related Mapper    : CustomerSearchMapper
 * Related DB Table  : customer_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CustomerSearchController, CustomerSearchService, CustomerSearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.CustomerStatus;
import com.plus33.erp.sales.entity.CustomerType;

public record CustomerSearchRequest(
    String query,
    Long companyId,
    CustomerType customerType,
    String pricingTier,
    CustomerStatus status,
    Integer paymentTermsDays,
    Boolean activeOnly
) {}
