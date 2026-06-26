package com.plus33.erp.finance.reporting.dto;

public record ReportWarning(
    String severity, // INFO, WARNING, ERROR
    String message,
    String code
) {}
