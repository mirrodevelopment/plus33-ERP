package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record DepreciationRunResponse(
    LocalDate targetDate,
    Boolean dryRun,
    Integer assetsProcessedCount,
    BigDecimal totalDepreciationAmount,
    List<DepreciationPreviewEntry> previewEntries,
    List<ProjectedJournalEntry> projectedJournalEntries
) {}
