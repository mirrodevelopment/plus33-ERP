/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetMaintenancePlanResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetMaintenancePlanController
 * Related Service   : AssetMaintenancePlanService, AssetMaintenancePlanServiceImpl
 * Related Repository: AssetMaintenancePlanRepository
 * Related Entity    : AssetMaintenancePlan
 * Related DTO       : AssetMaintenancePlanResponse
 * Related Mapper    : AssetMaintenancePlanMapper
 * Related DB Table  : asset_maintenance_plans
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetMaintenancePlanController, AssetMaintenancePlanService, AssetMaintenancePlanServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetMaintenancePlanResponse(
    Long id,
    Long fixedAssetId,
    String assetCode,
    String assetName,
    String planName,
    String frequency,
    LocalDate nextDueDate,
    BigDecimal estimatedCost,
    String assignedVendor,
    String notes,
    boolean isActive
) {}
