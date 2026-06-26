package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetWorkOrderRequest(
    Long fixedAssetId,
    String title,
    String description,
    String priority,
    LocalDate scheduledDate,
    BigDecimal estimatedCost
) {}
