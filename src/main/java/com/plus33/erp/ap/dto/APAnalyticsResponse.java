/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ap Module
 * Package           : com.plus33.erp.ap.dto
 * File              : APAnalyticsResponse.java
 * Purpose           : Data Transfer Object for request/response in Ap Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: APAnalyticsController
 * Related Service   : APAnalyticsService, APAnalyticsServiceImpl
 * Related Repository: APAnalyticsRepository
 * Related Entity    : APAnalytics
 * Related DTO       : APAnalyticsResponse, CashRequirementDTO, TopSupplierDTO
 * Related Mapper    : APAnalyticsMapper
 * Related DB Table  : a_p_analyticss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : APAnalyticsController, APAnalyticsService, APAnalyticsServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Ap Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.ap.dto;

import java.math.BigDecimal;
import java.util.List;

public record APAnalyticsResponse(
        Long companyId,
        BigDecimal averageInvoiceAmount,
        BigDecimal averageDaysToPay,
        BigDecimal earlyPaymentDiscounts,
        List<TopSupplierDTO> supplierConcentration,
        List<TopSupplierDTO> largestOutstandingSuppliers,
        CashRequirementDTO cashForecast
) {}
