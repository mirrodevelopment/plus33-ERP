package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetHistoryResponse(
    Long id,
    Long fixedAssetId,
    String eventType,
    LocalDate eventDate,
    String description,
    BigDecimal amount,
    Long referenceId,
    String performedBy
) {}
