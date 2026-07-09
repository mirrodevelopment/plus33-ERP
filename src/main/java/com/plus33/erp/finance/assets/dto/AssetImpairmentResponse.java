/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetImpairmentResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetImpairmentController
 * Related Service   : AssetImpairmentService, AssetImpairmentServiceImpl
 * Related Repository: AssetImpairmentRepository
 * Related Entity    : AssetImpairment
 * Related DTO       : AssetImpairmentResponse
 * Related Mapper    : AssetImpairmentMapper
 * Related DB Table  : asset_impairments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetImpairmentController, AssetImpairmentService, AssetImpairmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetImpairmentResponse(
    Long id,
    Long fixedAssetId,
    LocalDate impairmentDate,
    BigDecimal impairmentAmount,
    BigDecimal recoverableAmount,
    Long journalEntryId,
    String reason,
    String performedBy
) {}
