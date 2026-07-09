/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.dto
 * File              : PayrollDashboardSummaryResponse.java
 * Purpose           : Data Transfer Object for request/response in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollDashboardSummaryController
 * Related Service   : PayrollDashboardSummaryService, PayrollDashboardSummaryServiceImpl
 * Related Repository: PayrollDashboardSummaryRepository
 * Related Entity    : PayrollDashboardSummary
 * Related DTO       : PayrollDashboardSummaryResponse
 * Related Mapper    : PayrollDashboardSummaryMapper
 * Related DB Table  : payroll_dashboard_summarys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollDashboardSummaryController, PayrollDashboardSummaryService, PayrollDashboardSummaryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Workforce Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.workforce.dto;

import java.math.BigDecimal;

public record PayrollDashboardSummaryResponse(
        Long companyId,
        long totalPayrollRuns,
        BigDecimal aggregateGross,
        BigDecimal aggregateNet,
        BigDecimal aggregateEmployerContributions,
        BigDecimal aggregateTaxes
) {}
