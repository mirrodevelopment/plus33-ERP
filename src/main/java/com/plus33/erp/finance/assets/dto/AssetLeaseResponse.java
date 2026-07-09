/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetLeaseResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetLeaseController
 * Related Service   : AssetLeaseService, AssetLeaseServiceImpl
 * Related Repository: AssetLeaseRepository
 * Related Entity    : AssetLease
 * Related DTO       : AssetLeaseResponse
 * Related Mapper    : AssetLeaseMapper
 * Related DB Table  : asset_leases
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetLeaseController, AssetLeaseService, AssetLeaseServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetLeaseResponse(
    Long id,
    Long fixedAssetId,
    String leaseType,
    LocalDate leaseStartDate,
    LocalDate leaseEndDate,
    BigDecimal monthlyLeasePayment,
    String lessorName,
    Long leaseLiabilityAccountId
) {}
