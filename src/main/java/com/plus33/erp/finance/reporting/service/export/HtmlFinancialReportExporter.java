package com.plus33.erp.finance.reporting.service.export;

import com.plus33.erp.finance.reporting.dto.*;
import org.springframework.stereotype.Component;

@Component
public class HtmlFinancialReportExporter implements FinancialReportExporter {

    @Override
    public String getFormat() {
        return "HTML";
    }

    @Override
    public String exportTrialBalance(TrialBalanceResponse data, FinancialReportExportRequest context) {
        StringBuilder sb = new StringBuilder();
        appendHtmlHeader(sb, "Trial Balance");
        appendMetadataHtml(sb, "Trial Balance", context, data.generatedBy(), data.generatedAt().toString());

        sb.append("<div class='summary-row'>")
          .append("<p><strong>Number of Accounts:</strong> ").append(data.numberOfAccounts()).append("</p>")
          .append("<p><strong>Balanced:</strong> <span class='status-badge ")
          .append(data.balanced() ? "success" : "danger").append("'>")
          .append(data.balanced() ? "YES" : "NO").append("</span></p>");
        if (data.validationMessage() != null) {
            sb.append("<p><strong>Validation Message:</strong> ").append(escapeHtml(data.validationMessage())).append("</p>");
        }
        sb.append("</div>");

        // Warnings
        if (data.warnings() != null && !data.warnings().isEmpty()) {
            sb.append("<div class='warnings-box'>");
            sb.append("<h4>Warnings</h4><ul>");
            for (ReportWarning warning : data.warnings()) {
                sb.append("<li><strong class='").append(warning.severity().toLowerCase()).append("'>[")
                  .append(warning.severity()).append("]</strong> ")
                  .append(escapeHtml(warning.message())).append("</li>");
            }
            sb.append("</ul></div>");
        }

        // Table
        sb.append("<table>")
          .append("<thead><tr>")
          .append("<th>Account Code</th>")
          .append("<th>Account Name</th>")
          .append("<th>Account Type</th>")
          .append("<th>Opening Debit</th>")
          .append("<th>Opening Credit</th>")
          .append("<th>Period Debit</th>")
          .append("<th>Period Credit</th>")
          .append("<th>Closing Debit</th>")
          .append("<th>Closing Credit</th>")
          .append("</tr></thead><tbody>");

        for (TrialBalanceEntry entry : data.entries()) {
            sb.append("<tr>")
              .append("<td>").append(escapeHtml(entry.accountCode())).append("</td>")
              .append("<td>").append(escapeHtml(entry.accountName())).append("</td>")
              .append("<td>").append(escapeHtml(entry.accountType())).append("</td>")
              .append("<td class='number'>").append(entry.openingDebit()).append("</td>")
              .append("<td class='number'>").append(entry.openingCredit()).append("</td>")
              .append("<td class='number'>").append(entry.periodDebit()).append("</td>")
              .append("<td class='number'>").append(entry.periodCredit()).append("</td>")
              .append("<td class='number'>").append(entry.closingDebit()).append("</td>")
              .append("<td class='number'>").append(entry.closingCredit()).append("</td>")
              .append("</tr>");
        }

        // Totals
        sb.append("<tr class='total-row'>")
          .append("<td colspan='3'>Total</td>")
          .append("<td class='number'>").append(data.totalDebits()).append("</td>")
          .append("<td class='number'>").append(data.totalCredits()).append("</td>")
          .append("<td class='number'>-</td>")
          .append("<td class='number'>-</td>")
          .append("<td class='number'>").append(data.totalDebits()).append("</td>")
          .append("<td class='number'>").append(data.totalCredits()).append("</td>")
          .append("</tr>");

        sb.append("</tbody></table>");
        appendHtmlFooter(sb);
        return sb.toString();
    }

    @Override
    public String exportIncomeStatement(IncomeStatementResponse data, FinancialReportExportRequest context) {
        StringBuilder sb = new StringBuilder();
        appendHtmlHeader(sb, "Income Statement");
        appendMetadataHtml(sb, "Income Statement", context, data.generatedBy(), data.generatedAt().toString());

        sb.append("<h3>Revenues</h3>");
        sb.append("<table><thead><tr><th>Account Code</th><th>Account Name</th><th>Balance</th></tr></thead><tbody>");
        for (IncomeStatementEntry entry : data.revenues()) {
            sb.append("<tr>")
              .append("<td>").append(escapeHtml(entry.accountCode())).append("</td>")
              .append("<td>").append(escapeHtml(entry.accountName())).append("</td>")
              .append("<td class='number'>").append(entry.balance()).append("</td>")
              .append("</tr>");
        }
        sb.append("<tr class='total-row'><td colspan='2'>Total Revenue</td><td class='number'>").append(data.totalRevenue()).append("</td></tr>");
        sb.append("</tbody></table>");

        sb.append("<h3>Expenses</h3>");
        sb.append("<table><thead><tr><th>Account Code</th><th>Account Name</th><th>Balance</th></tr></thead><tbody>");
        for (IncomeStatementEntry entry : data.expenses()) {
            sb.append("<tr>")
              .append("<td>").append(escapeHtml(entry.accountCode())).append("</td>")
              .append("<td>").append(escapeHtml(entry.accountName())).append("</td>")
              .append("<td class='number'>").append(entry.balance()).append("</td>")
              .append("</tr>");
        }
        sb.append("<tr class='total-row'><td colspan='2'>Total Expenses</td><td class='number'>").append(data.totalExpenses()).append("</td></tr>");
        sb.append("</tbody></table>");

        sb.append("<div class='net-income-box'>")
          .append("<h2>Net Income: ").append(data.netIncome()).append("</h2>")
          .append("</div>");

        appendHtmlFooter(sb);
        return sb.toString();
    }

    @Override
    public String exportBalanceSheet(BalanceSheetResponse data, FinancialReportExportRequest context) {
        StringBuilder sb = new StringBuilder();
        appendHtmlHeader(sb, "Balance Sheet");
        appendMetadataHtml(sb, "Balance Sheet", context, data.generatedBy(), data.generatedAt().toString());

        sb.append("<div class='summary-row'>")
          .append("<p><strong>Balanced:</strong> <span class='status-badge ")
          .append(data.balanced() ? "success" : "danger").append("'>")
          .append(data.balanced() ? "YES" : "NO").append("</span></p>");
        if (data.validationMessage() != null) {
            sb.append("<p><strong>Validation Message:</strong> ").append(escapeHtml(data.validationMessage())).append("</p>");
        }
        sb.append("</div>");

        sb.append("<h3>Assets</h3>");
        sb.append("<table><thead><tr><th>Account Code</th><th>Account Name</th><th>Balance</th></tr></thead><tbody>");
        for (BalanceSheetEntry entry : data.assets()) {
            sb.append("<tr>")
              .append("<td>").append(escapeHtml(entry.accountCode())).append("</td>")
              .append("<td>").append(escapeHtml(entry.accountName())).append("</td>")
              .append("<td class='number'>").append(entry.balance()).append("</td>")
              .append("</tr>");
        }
        sb.append("<tr class='total-row'><td colspan='2'>Total Assets</td><td class='number'>").append(data.totalAssets()).append("</td></tr>");
        sb.append("</tbody></table>");

        sb.append("<h3>Liabilities</h3>");
        sb.append("<table><thead><tr><th>Account Code</th><th>Account Name</th><th>Balance</th></tr></thead><tbody>");
        for (BalanceSheetEntry entry : data.liabilities()) {
            sb.append("<tr>")
              .append("<td>").append(escapeHtml(entry.accountCode())).append("</td>")
              .append("<td>").append(escapeHtml(entry.accountName())).append("</td>")
              .append("<td class='number'>").append(entry.balance()).append("</td>")
              .append("</tr>");
        }
        sb.append("<tr class='total-row'><td colspan='2'>Total Liabilities</td><td class='number'>").append(data.totalLiabilities()).append("</td></tr>");
        sb.append("</tbody></table>");

        sb.append("<h3>Equity</h3>");
        sb.append("<table><thead><tr><th>Account Code</th><th>Account Name</th><th>Balance</th></tr></thead><tbody>");
        for (BalanceSheetEntry entry : data.equities()) {
            sb.append("<tr>")
              .append("<td>").append(escapeHtml(entry.accountCode())).append("</td>")
              .append("<td>").append(escapeHtml(entry.accountName())).append("</td>")
              .append("<td class='number'>").append(entry.balance()).append("</td>")
              .append("</tr>");
        }
        sb.append("<tr class='total-row'><td colspan='2'>Total Equity</td><td class='number'>").append(data.totalEquity()).append("</td></tr>");
        sb.append("</tbody></table>");

        sb.append("<div class='net-income-box'>")
          .append("<h2>Total Liabilities & Equity: ").append(data.totalLiabilities().add(data.totalEquity())).append("</h2>")
          .append("</div>");

        appendHtmlFooter(sb);
        return sb.toString();
    }

    private void appendHtmlHeader(StringBuilder sb, String title) {
        sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>").append(title).append("</title>")
          .append("<style>")
          .append("body { font-family: 'Helvetica Neue', Arial, sans-serif; color: #333; margin: 30px; line-height: 1.5; }")
          .append(".header { border-bottom: 2px solid #2b3a4a; padding-bottom: 10px; margin-bottom: 20px; }")
          .append("h1 { color: #2b3a4a; margin: 0; }")
          .append(".meta-grid { display: grid; grid-template-columns: repeat(2, 1fr); gap: 10px; background-color: #f8f9fa; padding: 15px; border: 1px solid #dee2e6; border-radius: 4px; margin-bottom: 20px; font-size: 14px; }")
          .append(".summary-row { background-color: #e9ecef; padding: 10px 15px; border-radius: 4px; margin-bottom: 20px; display: flex; gap: 30px; align-items: center; font-size: 15px; }")
          .append(".status-badge { padding: 3px 8px; border-radius: 3px; font-weight: bold; font-size: 12px; }")
          .append(".status-badge.success { background-color: #d4edda; color: #155724; }")
          .append(".status-badge.danger { background-color: #f8d7da; color: #721c24; }")
          .append("table { width: 100%; border-collapse: collapse; margin-bottom: 30px; font-size: 14px; }")
          .append("th, td { padding: 10px 12px; text-align: left; border-bottom: 1px solid #dee2e6; }")
          .append("th { background-color: #2b3a4a; color: #fff; font-weight: 600; }")
          .append(".number { text-align: right; }")
          .append(".total-row { font-weight: bold; background-color: #f1f3f5; border-top: 2px solid #2b3a4a; border-bottom: 2px solid #2b3a4a; }")
          .append(".warnings-box { border: 1px solid #ffeeba; background-color: #fff3cd; color: #856404; padding: 15px; border-radius: 4px; margin-bottom: 20px; }")
          .append(".warnings-box h4 { margin-top: 0; margin-bottom: 10px; }")
          .append(".warnings-box ul { margin: 0; padding-left: 20px; }")
          .append("strong.error { color: #dc3545; }")
          .append("strong.warning { color: #ffc107; }")
          .append("strong.info { color: #17a2b8; }")
          .append(".net-income-box { background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; padding: 15px; border-radius: 4px; text-align: right; margin-top: 20px; }")
          .append(".net-income-box h2 { margin: 0; }")
          .append("@media print { body { margin: 0; font-size: 12px; } .header { margin-bottom: 15px; } .meta-grid { padding: 10px; margin-bottom: 15px; } table { margin-bottom: 20px; } th { background-color: #e9ecef; color: #000; border-bottom: 2px solid #000; } }")
          .append("</style></head><body>");
    }

    private void appendMetadataHtml(StringBuilder sb, String reportName, FinancialReportExportRequest context, String user, String timestamp) {
        sb.append("<div class='header'>")
          .append("<h1>").append(reportName).append("</h1>")
          .append("</div>")
          .append("<div class='meta-grid'>")
          .append("<div><strong>Company ID:</strong> ").append(context.companyId()).append("</div>")
          .append("<div><strong>Start Date:</strong> ").append(context.startDate()).append("</div>")
          .append("<div><strong>End Date:</strong> ").append(context.endDate()).append("</div>")
          .append("<div><strong>Currency:</strong> ").append(context.currency() != null ? context.currency() : "Functional Base").append("</div>")
          .append("<div><strong>Exchange Rate Type:</strong> ").append(context.rateType() != null ? context.rateType() : "N/A").append("</div>")
          .append("<div><strong>Exclude Closing:</strong> ").append(context.excludeClosing()).append("</div>")
          .append("<div><strong>Generated By:</strong> ").append(escapeHtml(user)).append("</div>")
          .append("<div><strong>Generated At:</strong> ").append(escapeHtml(timestamp)).append("</div>")
          .append("</div>");
    }

    private void appendHtmlFooter(StringBuilder sb) {
        sb.append("</body></html>");
    }

    private String escapeHtml(String val) {
        if (val == null) {
            return "";
        }
        return val.replace("&", "&amp;")
                  .replace("<", "&lt;")
                  .replace(">", "&gt;")
                  .replace("\"", "&quot;")
                  .replace("'", "&#37;");
    }
}
