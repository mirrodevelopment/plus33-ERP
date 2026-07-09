/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : SalesOrderSearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesOrderSearchController
 * Related Service   : SalesOrderSearchService, SalesOrderSearchServiceImpl
 * Related Repository: SalesOrderSearchRepository
 * Related Entity    : SalesOrderSearch
 * Related DTO       : SalesOrderSearchRequest
 * Related Mapper    : SalesOrderSearchMapper
 * Related DB Table  : sales_order_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SalesOrderSearchController, SalesOrderSearchService, SalesOrderSearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.SalesOrderStatus;

import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code SalesOrderSearchRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record SalesOrderSearchRequest(
    String query,
    Long companyId,
    Long customerId,
    SalesOrderStatus status,
    String customerType,
    Boolean creditOverride,
    LocalDate requestedDeliveryDateFrom,
    LocalDate requestedDeliveryDateTo,
    Long createdBy,
    Long approvedBy
) {}
