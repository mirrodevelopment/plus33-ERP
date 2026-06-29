package com.plus33.erp.workforce.dto;

import com.plus33.erp.workforce.entity.PayrollCalendarType;

public record PayrollRunRequest(
        Long companyId,
        Long payrollPeriodId,
        String runNumber,
        PayrollCalendarType calendarType,
        String countryCode,
        String runType
) {}
