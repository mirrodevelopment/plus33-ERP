package com.plus33.erp.finance.reporting.service.export;

import com.plus33.erp.finance.reporting.dto.*;

public interface FinancialReportExporter {
    String getFormat(); // "CSV", "HTML"
    String exportTrialBalance(TrialBalanceResponse data, FinancialReportExportRequest context);
    String exportIncomeStatement(IncomeStatementResponse data, FinancialReportExportRequest context);
    String exportBalanceSheet(BalanceSheetResponse data, FinancialReportExportRequest context);
}
