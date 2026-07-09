/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ap Module
 * Package           : com.plus33.erp.ap.dto
 * File              : APSummaryResponse.java
 * Purpose           : Data Transfer Object for request/response in Ap Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: APSummaryController
 * Related Service   : APSummaryService, APSummaryServiceImpl
 * Related Repository: APSummaryRepository
 * Related Entity    : APSummary
 * Related DTO       : APSummaryResponse
 * Related Mapper    : APSummaryMapper
 * Related DB Table  : a_p_summarys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : APSummaryController, APSummaryService, APSummaryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Ap Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.ap.dto;

import java.math.BigDecimal;
import java.util.List;

public record APSummaryResponse(
        Long companyId,
        Long totalBills,
        BigDecimal totalBilledAmount,
        BigDecimal totalPaidAmount,
        BigDecimal totalOutstandingAmount,
        List<APStatusBucket> billsByStatus
) {}
