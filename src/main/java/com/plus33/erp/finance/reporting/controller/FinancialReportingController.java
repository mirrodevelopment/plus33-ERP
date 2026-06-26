package com.plus33.erp.finance.reporting.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.reporting.dto.*;
import com.plus33.erp.finance.reporting.service.FinancialReportingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/financial-reports")
@RequiredArgsConstructor
@Tag(name = "Financial Reporting Management", description = "REST APIs for Trial Balance, Income Statement, Balance Sheet, Period Locking, and Fiscal Year Closing")
public class FinancialReportingController {

    private final FinancialReportingService financialReportingService;

    @GetMapping("/trial-balance")
    @PreAuthorize("hasAuthority('FINANCIAL_REPORT_VIEW')")
    @Operation(summary = "Get Trial Balance", description = "Generates a dynamic, mathematically validated Trial Balance statement.")
    public ResponseEntity<ApiResponse<TrialBalanceResponse>> getTrialBalance(
            @RequestParam Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String rateType,
            @RequestParam(required = false, defaultValue = "false") boolean excludeClosing
    ) {
        TrialBalanceResponse response = financialReportingService.getTrialBalance(companyId, startDate, endDate, currency, rateType, excludeClosing);
        return ResponseEntity.ok(ApiResponse.success("Trial Balance generated successfully", response));
    }

    @GetMapping("/income-statement")
    @PreAuthorize("hasAuthority('FINANCIAL_REPORT_VIEW')")
    @Operation(summary = "Get Income Statement", description = "Generates a dynamic Profit & Loss statement for a specific period.")
    public ResponseEntity<ApiResponse<IncomeStatementResponse>> getIncomeStatement(
            @RequestParam Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String rateType,
            @RequestParam(required = false, defaultValue = "false") boolean excludeClosing
    ) {
        IncomeStatementResponse response = financialReportingService.getIncomeStatement(companyId, startDate, endDate, currency, rateType, excludeClosing);
        return ResponseEntity.ok(ApiResponse.success("Income Statement generated successfully", response));
    }

    @GetMapping("/balance-sheet")
    @PreAuthorize("hasAuthority('FINANCIAL_REPORT_VIEW')")
    @Operation(summary = "Get Balance Sheet", description = "Generates a dynamic Balance Sheet statement as of a specific date.")
    public ResponseEntity<ApiResponse<BalanceSheetResponse>> getBalanceSheet(
            @RequestParam Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate asOfDate,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String rateType,
            @RequestParam(required = false, defaultValue = "false") boolean excludeClosing
    ) {
        BalanceSheetResponse response = financialReportingService.getBalanceSheet(companyId, asOfDate, currency, rateType, excludeClosing);
        return ResponseEntity.ok(ApiResponse.success("Balance Sheet generated successfully", response));
    }

    @GetMapping("/export")
    @PreAuthorize("hasAuthority('FINANCIAL_REPORT_EXPORT')")
    @Operation(summary = "Export Financial Report", description = "Exports Trial Balance, Income Statement, or Balance Sheet to CSV or print-friendly HTML.")
    public ResponseEntity<String> exportReport(
            @RequestParam Long companyId,
            @RequestParam String reportType, // TRIAL_BALANCE, INCOME_STATEMENT, BALANCE_SHEET
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false) String currency,
            @RequestParam(required = false) String rateType,
            @RequestParam(required = false, defaultValue = "false") boolean excludeClosing,
            @RequestParam(required = false, defaultValue = "CSV") String format // CSV, HTML
    ) {
        // Default date boundaries if not specified
        LocalDate start = startDate != null ? startDate : LocalDate.of(LocalDate.now().getYear(), 1, 1);
        LocalDate end = endDate != null ? endDate : LocalDate.now();

        String exported = financialReportingService.exportReport(companyId, reportType, start, end, currency, rateType, excludeClosing, format);

        String contentType = format.equalsIgnoreCase("HTML") ? "text/html" : "text/csv";
        String filename = reportType.toLowerCase() + "_" + start + "_to_" + end + "." + format.toLowerCase();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, contentType + "; charset=UTF-8")
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(exported);
    }

    @PostMapping("/period-lock")
    @PreAuthorize("hasAuthority('PERIOD_LOCK_CREATE')")
    @Operation(summary = "Lock Accounting Period", description = "Locks or unlocks an accounting period for a company (supports SOFT and HARD locks).")
    public ResponseEntity<ApiResponse<PeriodLockResponse>> lockPeriod(
            @RequestParam Long companyId,
            @RequestBody PeriodLockRequest request
    ) {
        PeriodLockResponse response = financialReportingService.lockPeriod(companyId, request);
        return ResponseEntity.ok(ApiResponse.success("Period lock updated successfully", response));
    }

    @GetMapping("/period-lock")
    @PreAuthorize("hasAuthority('PERIOD_LOCK_VIEW')")
    @Operation(summary = "Get Period Lock Status", description = "Retrieves the current period lock settings for a company.")
    public ResponseEntity<ApiResponse<PeriodLockResponse>> getPeriodLock(
            @RequestParam Long companyId
    ) {
        PeriodLockResponse response = financialReportingService.getPeriodLock(companyId);
        return ResponseEntity.ok(ApiResponse.success("Period lock settings retrieved successfully", response));
    }

    @PostMapping("/fiscal-year-close")
    @PreAuthorize("hasAuthority('FISCAL_YEAR_CLOSE')")
    @Operation(summary = "Close Fiscal Year", description = "Performs year-end closing, zeroing out temporary accounts and posting net income to Retained Earnings.")
    public ResponseEntity<ApiResponse<FiscalYearCloseResponse>> closeFiscalYear(
            @RequestParam Long companyId,
            @RequestBody FiscalYearCloseRequest request
    ) {
        FiscalYearCloseResponse response = financialReportingService.closeFiscalYear(companyId, request);
        return ResponseEntity.ok(ApiResponse.success("Fiscal year closed successfully", response));
    }

    @PostMapping("/fiscal-year-reopen/{fiscalYear}")
    @PreAuthorize("hasAuthority('FISCAL_YEAR_REOPEN')")
    @Operation(summary = "Reopen Fiscal Year", description = "Reopens a closed fiscal year by posting an automatic reversing journal entry and shifting the period lock.")
    public ResponseEntity<ApiResponse<Void>> reopenFiscalYear(
            @RequestParam Long companyId,
            @PathVariable Integer fiscalYear
    ) {
        financialReportingService.reopenFiscalYear(companyId, fiscalYear);
        return ResponseEntity.ok(ApiResponse.success("Fiscal year reopened successfully", null));
    }
}
