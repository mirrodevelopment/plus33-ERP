/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetReservationRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetReservationController
 * Related Service   : AssetReservationService, AssetReservationServiceImpl
 * Related Repository: AssetReservationRepository
 * Related Entity    : AssetReservation
 * Related DTO       : AssetReservationRequest
 * Related Mapper    : AssetReservationMapper
 * Related DB Table  : asset_reservations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetReservationController, AssetReservationService, AssetReservationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetReservationRequest(
    Long fixedAssetId,
    String reservedForEmployee,
    String reservedForDepartment,
    LocalDate startDate,
    LocalDate endDate,
    String purpose
) {}
