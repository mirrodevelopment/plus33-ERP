/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.service.export
 * File              : FinancialReportExporter.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FinancialReportExporterController
 * Related Service   : FinancialReportExporterService, FinancialReportExporterServiceImpl
 * Related Repository: FinancialReportExporterRepository
 * Related Entity    : FinancialReportExporter
 * Related DTO       : BalanceSheetResponse, FinancialReportExportRequest, IncomeStatementResponse, TrialBalanceResponse
 * Related Mapper    : FinancialReportExporterMapper
 * Related DB Table  : financial_report_exporters
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.service.export;

import com.plus33.erp.finance.reporting.dto.*;

public interface FinancialReportExporter {
    String getFormat(); // "CSV", "HTML"
    String exportTrialBalance(TrialBalanceResponse data, FinancialReportExportRequest context);
    String exportIncomeStatement(IncomeStatementResponse data, FinancialReportExportRequest context);
    String exportBalanceSheet(BalanceSheetResponse data, FinancialReportExportRequest context);
}
