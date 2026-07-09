/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetUtilizationResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetUtilizationController
 * Related Service   : AssetUtilizationService, AssetUtilizationServiceImpl
 * Related Repository: AssetUtilizationRepository
 * Related Entity    : AssetUtilization
 * Related DTO       : AssetUtilizationResponse
 * Related Mapper    : AssetUtilizationMapper
 * Related DB Table  : asset_utilizations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetUtilizationController, AssetUtilizationService, AssetUtilizationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetUtilizationResponse(
    Long id,
    Long fixedAssetId,
    LocalDate recordDate,
    BigDecimal hoursUsed,
    BigDecimal outputUnits,
    String notes
) {}
