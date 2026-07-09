/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : FixedAssetDashboardResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetDashboardController
 * Related Service   : FixedAssetDashboardService, FixedAssetDashboardServiceImpl
 * Related Repository: FixedAssetDashboardRepository
 * Related Entity    : FixedAssetDashboard
 * Related DTO       : FixedAssetDashboardResponse
 * Related Mapper    : FixedAssetDashboardMapper
 * Related DB Table  : fixed_asset_dashboards
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetDashboardController, FixedAssetDashboardService, FixedAssetDashboardServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
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
