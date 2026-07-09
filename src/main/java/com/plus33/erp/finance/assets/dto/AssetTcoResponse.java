/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetTcoResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetTcoController
 * Related Service   : AssetTcoService, AssetTcoServiceImpl
 * Related Repository: AssetTcoRepository
 * Related Entity    : AssetTco
 * Related DTO       : AssetTcoResponse
 * Related Mapper    : AssetTcoMapper
 * Related DB Table  : asset_tcos
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetTcoController, AssetTcoService, AssetTcoServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;

public record AssetTcoResponse(
    Long fixedAssetId,
    String assetCode,
    String assetName,
    BigDecimal acquisitionCost,
    BigDecimal capitalizedMaintenance,
    BigDecimal operatingMaintenance,
    BigDecimal totalDepreciation,
    BigDecimal totalInsurance,
    BigDecimal totalDowntimeCost,
    BigDecimal totalCostOfOwnership,
    BigDecimal costPerOperatingHour,
    BigDecimal costPerUnit,
    Integer totalDowntimeHours,
    Integer totalOperatingHours
) {}
