/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.service
 * File              : FinancialReportingService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FinancialReportingController
 * Related Service   : FinancialReportingService, FinancialReportingServiceImpl
 * Related Repository: FinancialReportingRepository
 * Related Entity    : FinancialReporting
 * Related DTO       : BalanceSheetResponse, FiscalYearCloseRequest, FiscalYearCloseResponse, IncomeStatementResponse, PeriodLockRequest
 * Related Mapper    : FinancialReportingMapper
 * Related DB Table  : financial_reportings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.service;

import com.plus33.erp.finance.reporting.dto.*;
import java.time.LocalDate;

public interface FinancialReportingService {
    TrialBalanceResponse getTrialBalance(Long companyId, LocalDate startDate, LocalDate endDate, String currency, String rateType, boolean excludeClosing);
    IncomeStatementResponse getIncomeStatement(Long companyId, LocalDate startDate, LocalDate endDate, String currency, String rateType, boolean excludeClosing);
    BalanceSheetResponse getBalanceSheet(Long companyId, LocalDate asOfDate, String currency, String rateType, boolean excludeClosing);
    
    String exportReport(Long companyId, String reportType, LocalDate startDate, LocalDate endDate, String currency, String rateType, boolean excludeClosing, String format);
    
    PeriodLockResponse lockPeriod(Long companyId, PeriodLockRequest request);
    PeriodLockResponse getPeriodLock(Long companyId);
    
    FiscalYearCloseResponse closeFiscalYear(Long companyId, FiscalYearCloseRequest request);
    void reopenFiscalYear(Long companyId, Integer fiscalYear);
}
