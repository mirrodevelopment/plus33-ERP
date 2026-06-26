package com.plus33.erp.finance.reporting.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record PeriodLockResponse(
    Long id,
    Long companyId,
    LocalDate lockDate,
    String lockType,
    String lockedBy,
    LocalDateTime lockedAt,
    String reason
) {}
