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
