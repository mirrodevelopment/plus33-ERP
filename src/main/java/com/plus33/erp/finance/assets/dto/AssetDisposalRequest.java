/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetDisposalRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetDisposalController
 * Related Service   : AssetDisposalService, AssetDisposalServiceImpl
 * Related Repository: AssetDisposalRepository
 * Related Entity    : AssetDisposal
 * Related DTO       : AssetDisposalRequest
 * Related Mapper    : AssetDisposalMapper
 * Related DB Table  : asset_disposals
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetDisposalController, AssetDisposalService, AssetDisposalServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetDisposalRequest(
    LocalDate disposalDate,
    String disposalType, // DISPOSED, WRITTEN_OFF
    BigDecimal proceeds,
    String reason
) {}
