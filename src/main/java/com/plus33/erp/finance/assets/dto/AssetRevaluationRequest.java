/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetRevaluationRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetRevaluationController
 * Related Service   : AssetRevaluationService, AssetRevaluationServiceImpl
 * Related Repository: AssetRevaluationRepository
 * Related Entity    : AssetRevaluation
 * Related DTO       : AssetRevaluationRequest
 * Related Mapper    : AssetRevaluationMapper
 * Related DB Table  : asset_revaluations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetRevaluationController, AssetRevaluationService, AssetRevaluationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetRevaluationRequest(
    LocalDate revaluationDate,
    BigDecimal newFairValue,
    Long revaluationReserveAccountId,
    String reason
) {}
