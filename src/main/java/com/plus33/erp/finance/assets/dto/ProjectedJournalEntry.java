package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ProjectedJournalEntry(
    String entryNumber,
    LocalDate entryDate,
    String description,
    BigDecimal totalAmount,
    List<ProjectedJournalEntryLine> lines
) {}
