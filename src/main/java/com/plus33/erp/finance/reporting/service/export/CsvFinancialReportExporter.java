package com.plus33.erp.finance.reporting.service.export;

import com.plus33.erp.finance.reporting.dto.*;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;

@Component
public class CsvFinancialReportExporter implements FinancialReportExporter {

    @Override
    public String getFormat() {
        return "CSV";
    }

    @Override
    public String exportTrialBalance(TrialBalanceResponse data, FinancialReportExportRequest context) {
        StringBuilder sb = new StringBuilder();
        // 1. Metadata Header
        appendMetadata(sb, "Trial Balance", context, data.generatedBy(), data.generatedAt().toString());
        sb.append("Number of Accounts: ").append(data.numberOfAccounts()).append("\n");
        sb.append("Balanced: ").append(data.balanced()).append("\n");
        if (data.validationMessage() != null) {
            sb.append("Validation Message: ").append(data.validationMessage().replace(",", ";")).append("\n");
        }
        sb.append("\n");

        // 2. Report Headers
        sb.append("Account Code,Account Name,Account Type,Opening Debit,Opening Credit,Period Debit,Period Credit,Closing Debit,Closing Credit\n");

        // 3. Entries
        for (TrialBalanceEntry entry : data.entries()) {
            sb.append(escapeCsv(entry.accountCode())).append(",")
              .append(escapeCsv(entry.accountName())).append(",")
              .append(escapeCsv(entry.accountType())).append(",")
              .append(entry.openingDebit()).append(",")
              .append(entry.openingCredit()).append(",")
              .append(entry.periodDebit()).append(",")
              .append(entry.periodCredit()).append(",")
              .append(entry.closingDebit()).append(",")
              .append(entry.closingCredit()).append("\n");
        }

        // 4. Totals
        sb.append("Total,,-,").append(data.totalDebits()).append(",")
          .append(data.totalCredits()).append(",-,-,")
          .append(data.totalDebits()).append(",")
          .append(data.totalCredits()).append("\n");

        return sb.toString();
    }

    @Override
    public String exportIncomeStatement(IncomeStatementResponse data, FinancialReportExportRequest context) {
        StringBuilder sb = new StringBuilder();
        appendMetadata(sb, "Income Statement", context, data.generatedBy(), data.generatedAt().toString());
        sb.append("\n");

        sb.append("Revenues\n");
        sb.append("Account Code,Account Name,Balance\n");
        for (IncomeStatementEntry entry : data.revenues()) {
            sb.append(escapeCsv(entry.accountCode())).append(",")
              .append(escapeCsv(entry.accountName())).append(",")
              .append(entry.balance()).append("\n");
        }
        sb.append("Total Revenue,, ").append(data.totalRevenue()).append("\n\n");

        sb.append("Expenses\n");
        sb.append("Account Code,Account Name,Balance\n");
        for (IncomeStatementEntry entry : data.expenses()) {
            sb.append(escapeCsv(entry.accountCode())).append(",")
              .append(escapeCsv(entry.accountName())).append(",")
              .append(entry.balance()).append("\n");
        }
        sb.append("Total Expenses,, ").append(data.totalExpenses()).append("\n\n");

        sb.append("Net Income,, ").append(data.netIncome()).append("\n");

        return sb.toString();
    }

    @Override
    public String exportBalanceSheet(BalanceSheetResponse data, FinancialReportExportRequest context) {
        StringBuilder sb = new StringBuilder();
        appendMetadata(sb, "Balance Sheet", context, data.generatedBy(), data.generatedAt().toString());
        sb.append("Balanced: ").append(data.balanced()).append("\n");
        if (data.validationMessage() != null) {
            sb.append("Validation Message: ").append(data.validationMessage().replace(",", ";")).append("\n");
        }
        sb.append("\n");

        sb.append("Assets\n");
        sb.append("Account Code,Account Name,Balance\n");
        for (BalanceSheetEntry entry : data.assets()) {
            sb.append(escapeCsv(entry.accountCode())).append(",")
              .append(escapeCsv(entry.accountName())).append(",")
              .append(entry.balance()).append("\n");
        }
        sb.append("Total Assets,, ").append(data.totalAssets()).append("\n\n");

        sb.append("Liabilities\n");
        sb.append("Account Code,Account Name,Balance\n");
        for (BalanceSheetEntry entry : data.liabilities()) {
            sb.append(escapeCsv(entry.accountCode())).append(",")
              .append(escapeCsv(entry.accountName())).append(",")
              .append(entry.balance()).append("\n");
        }
        sb.append("Total Liabilities,, ").append(data.totalLiabilities()).append("\n\n");

        sb.append("Equity\n");
        sb.append("Account Code,Account Name,Balance\n");
        for (BalanceSheetEntry entry : data.equities()) {
            sb.append(escapeCsv(entry.accountCode())).append(",")
              .append(escapeCsv(entry.accountName())).append(",")
              .append(entry.balance()).append("\n");
        }
        sb.append("Total Equity,, ").append(data.totalEquity()).append("\n\n");

        sb.append("Total Liabilities & Equity,, ").append(data.totalLiabilities().add(data.totalEquity())).append("\n");

        return sb.toString();
    }

    private void appendMetadata(StringBuilder sb, String reportName, FinancialReportExportRequest context, String user, String timestamp) {
        sb.append("Report Name: ").append(reportName).append("\n");
        sb.append("Company ID: ").append(context.companyId()).append("\n");
        sb.append("Start Date: ").append(context.startDate()).append("\n");
        sb.append("End Date: ").append(context.endDate()).append("\n");
        sb.append("Currency: ").append(context.currency() != null ? context.currency() : "Functional Base").append("\n");
        sb.append("Exchange Rate Type: ").append(context.rateType() != null ? context.rateType() : "N/A").append("\n");
        sb.append("Exclude Closing: ").append(context.excludeClosing()).append("\n");
        sb.append("Generated By: ").append(user).append("\n");
        sb.append("Generated At: ").append(timestamp).append("\n");
    }

    private String escapeCsv(String val) {
        if (val == null) {
            return "";
        }
        if (val.contains(",") || val.contains("\"") || val.contains("\n")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }
}
