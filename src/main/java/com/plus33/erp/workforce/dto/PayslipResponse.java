package com.plus33.erp.workforce.dto;

import java.math.BigDecimal;

public record PayslipResponse(
        Long employeeId,
        String employeeName,
        String runNumber,
        BigDecimal grossPay,
        BigDecimal netPay,
        BigDecimal deductions,
        BigDecimal taxWithheld,
        String currencyCode
) {}
