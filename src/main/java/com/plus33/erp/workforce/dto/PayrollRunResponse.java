package com.plus33.erp.workforce.dto;

import com.plus33.erp.workforce.entity.PayrollCalendarType;
import com.plus33.erp.workforce.entity.PayrollRunStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PayrollRunResponse(
        Long id,
        Long companyId,
        String runNumber,
        PayrollCalendarType calendarType,
        String countryCode,
        String runType,
        PayrollRunStatus status,
        BigDecimal totalGross,
        BigDecimal totalNet,
        BigDecimal totalEmployerContributions,
        BigDecimal totalTaxes,
        String executedBy,
        String approvedBy,
        LocalDateTime postedAt,
        LocalDateTime paidAt,
        LocalDateTime createdAt
) {}
