package com.plus33.erp.finance.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record JournalEntryResponse(
        Long id,
        String entryNumber,
        Long companyId,
        LocalDate entryDate,
        String description,
        String sourceModule,
        String sourceReference,
        String status,
        String currencyCode,
        LocalDateTime postedAt,
        List<JournalEntryLineResponse> lines
) {}
