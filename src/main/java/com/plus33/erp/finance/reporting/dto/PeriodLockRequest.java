package com.plus33.erp.finance.reporting.dto;

import java.time.LocalDate;

public record PeriodLockRequest(
    LocalDate lockDate,
    String lockType, // SOFT, HARD
    String reason
) {}
