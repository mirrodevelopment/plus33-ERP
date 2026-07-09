/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetPartialDisposalRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetPartialDisposalController
 * Related Service   : AssetPartialDisposalService, AssetPartialDisposalServiceImpl
 * Related Repository: AssetPartialDisposalRepository
 * Related Entity    : AssetPartialDisposal
 * Related DTO       : AssetPartialDisposalRequest
 * Related Mapper    : AssetPartialDisposalMapper
 * Related DB Table  : asset_partial_disposals
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetPartialDisposalController, AssetPartialDisposalService, AssetPartialDisposalServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetPartialDisposalRequest(
    BigDecimal disposalAmount,
    LocalDate disposalDate,
    BigDecimal saleProceeds,
    String reason
) {}
