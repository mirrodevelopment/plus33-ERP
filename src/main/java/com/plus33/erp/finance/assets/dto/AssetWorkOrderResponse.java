package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetWorkOrderResponse(
    Long id,
    Long fixedAssetId,
    String assetCode,
    String assetName,
    String title,
    String description,
    String priority,
    String status,
    LocalDate scheduledDate,
    LocalDate completedDate,
    BigDecimal estimatedCost,
    BigDecimal actualCost,
    String createdBy
) {}
