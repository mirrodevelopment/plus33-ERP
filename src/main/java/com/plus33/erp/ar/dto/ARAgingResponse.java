/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ar Module
 * Package           : com.plus33.erp.ar.dto
 * File              : ARAgingResponse.java
 * Purpose           : Data Transfer Object for request/response in Ar Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ARAgingController
 * Related Service   : ARAgingService, ARAgingServiceImpl
 * Related Repository: ARAgingRepository
 * Related Entity    : ARAging
 * Related DTO       : ARAgingResponse
 * Related Mapper    : ARAgingMapper
 * Related DB Table  : a_r_agings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ARAgingController, ARAgingService, ARAgingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Ar Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.ar.dto;

import java.math.BigDecimal;

/**
 * One row from mv_customer_aging.
 */
public record ARAgingResponse(
        Long companyId,
        Long customerId,
        String customerName,
        BigDecimal totalOutstanding,
        BigDecimal agingCurrent,
        BigDecimal aging1to30,
        BigDecimal aging31to60,
        BigDecimal aging61to90,
        BigDecimal aging90Plus
) {}
