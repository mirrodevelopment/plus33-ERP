/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetCapitalizeCwipRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetCapitalizeCwipController
 * Related Service   : AssetCapitalizeCwipService, AssetCapitalizeCwipServiceImpl
 * Related Repository: AssetCapitalizeCwipRepository
 * Related Entity    : AssetCapitalizeCwip
 * Related DTO       : AssetCapitalizeCwipRequest
 * Related Mapper    : AssetCapitalizeCwipMapper
 * Related DB Table  : asset_capitalize_cwips
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetCapitalizeCwipController, AssetCapitalizeCwipService, AssetCapitalizeCwipServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetCapitalizeCwipRequest(
    LocalDate capitalizationDate,
    BigDecimal totalCapitalizedCost,
    Long assetCategoryId,
    Integer usefulLifeYears,
    BigDecimal salvageValue,
    String depreciationMethod
) {}
