package com.plus33.erp.workforce.dto;

import java.math.BigDecimal;

public record PayrollDashboardSummaryResponse(
        Long companyId,
        long totalPayrollRuns,
        BigDecimal aggregateGross,
        BigDecimal aggregateNet,
        BigDecimal aggregateEmployerContributions,
        BigDecimal aggregateTaxes
) {}
