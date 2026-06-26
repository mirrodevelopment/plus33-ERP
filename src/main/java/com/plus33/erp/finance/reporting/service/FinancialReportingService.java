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
