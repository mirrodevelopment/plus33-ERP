package com.plus33.erp.finance.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record JournalEntryRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        @NotNull(message = "Entry date is required")
        LocalDate entryDate,

        String description,

        String currencyCode,

        @NotEmpty(message = "Journal entry must contain at least one line")
        @Valid
        List<JournalEntryLineRequest> lines
) {}
