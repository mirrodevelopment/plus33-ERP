/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.dto
 * File              : PayablesAgingResponse.java
 * Purpose           : Data Transfer Object for request/response in Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayablesAgingController
 * Related Service   : PayablesAgingService, PayablesAgingServiceImpl
 * Related Repository: PayablesAgingRepository
 * Related Entity    : PayablesAging
 * Related DTO       : PayablesAgingResponse
 * Related Mapper    : PayablesAgingMapper
 * Related DB Table  : payables_agings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayablesAgingController, PayablesAgingService, PayablesAgingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Analytics Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;

public record PayablesAgingResponse(
        Long companyId,
        Long supplierId,
        String supplierName,
        BigDecimal totalOutstanding,
        BigDecimal agingCurrent,
        BigDecimal aging1To30,
        BigDecimal aging31To60,
        BigDecimal aging61To90,
        BigDecimal aging90Plus
) {}
