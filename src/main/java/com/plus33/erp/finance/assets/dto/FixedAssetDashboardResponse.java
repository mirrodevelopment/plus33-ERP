package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.util.Map;

public record FixedAssetDashboardResponse(
    BigDecimal totalAssetValue,
    BigDecimal netBookValue,
    BigDecimal monthlyDepreciation,
    Map<String, BigDecimal> categoryBreakdown,
    Integer assetsNearEndOfLifeCount,
    Integer warrantyExpiringCount,
    Integer insuranceExpiringCount,
    Integer assetsUnderMaintenanceCount,
    Map<String, BigDecimal> locationBreakdown
) {}
