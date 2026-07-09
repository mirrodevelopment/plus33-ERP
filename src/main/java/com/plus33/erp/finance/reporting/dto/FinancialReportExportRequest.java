/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.dto
 * File              : FinancialReportExportRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FinancialReportExportController
 * Related Service   : FinancialReportExportService, FinancialReportExportServiceImpl
 * Related Repository: FinancialReportExportRepository
 * Related Entity    : FinancialReportExport
 * Related DTO       : FinancialReportExportRequest
 * Related Mapper    : FinancialReportExportMapper
 * Related DB Table  : financial_report_exports
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FinancialReportExportController, FinancialReportExportService, FinancialReportExportServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.dto;

import java.time.LocalDate;

public record FinancialReportExportRequest(
    Long companyId,
    String reportType,
    LocalDate startDate,
    LocalDate endDate,
    String currency,
    String rateType,
    boolean excludeClosing
) {}
