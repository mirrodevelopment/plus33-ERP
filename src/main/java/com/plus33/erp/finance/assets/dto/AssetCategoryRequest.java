/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetCategoryRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetCategoryController
 * Related Service   : AssetCategoryService, AssetCategoryServiceImpl
 * Related Repository: AssetCategoryRepository
 * Related Entity    : AssetCategory
 * Related DTO       : AssetCategoryRequest
 * Related Mapper    : AssetCategoryMapper
 * Related DB Table  : asset_categorys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetCategoryController, AssetCategoryService, AssetCategoryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;

public record AssetCategoryRequest(
    String code,
    String name,
    String depreciationMethod, // STRAIGHT_LINE, WDV, NONE
    BigDecimal depreciationRate,
    Integer usefulLifeYears,
    Long assetAccountId,
    Long accumulatedDepreciationAccountId,
    Long depreciationExpenseAccountId,
    Long gainLossAccountId
) {}
