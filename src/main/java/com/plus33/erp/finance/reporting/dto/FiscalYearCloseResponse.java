package com.plus33.erp.finance.reporting.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record FiscalYearCloseResponse(
    Long id,
    Long companyId,
    Integer fiscalYear,
    LocalDate startDate,
    LocalDate endDate,
    String status,
    Long closingJournalId,
    String closedBy,
    LocalDateTime closedAt
) {}
