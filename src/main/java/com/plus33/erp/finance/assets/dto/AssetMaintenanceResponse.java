package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record AssetMaintenanceResponse(
    Long id,
    Long fixedAssetId,
    LocalDate maintenanceDate,
    String description,
    BigDecimal cost,
    Boolean capitalize,
    Long journalEntryId,
    String performedBy,
    LocalDateTime createdAt
) {}
