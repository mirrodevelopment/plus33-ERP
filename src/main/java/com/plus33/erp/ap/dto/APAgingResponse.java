/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ap Module
 * Package           : com.plus33.erp.ap.dto
 * File              : APAgingResponse.java
 * Purpose           : Data Transfer Object for request/response in Ap Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: APAgingController
 * Related Service   : APAgingService, APAgingServiceImpl
 * Related Repository: APAgingRepository
 * Related Entity    : APAging
 * Related DTO       : APAgingResponse
 * Related Mapper    : APAgingMapper
 * Related DB Table  : a_p_agings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : APAgingController, APAgingService, APAgingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Ap Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.ap.dto;

import java.math.BigDecimal;
import java.util.List;

public record APAgingResponse(
        Long supplierId,
        String supplierName,
        Long companyId,
        BigDecimal totalOutstanding,
        List<APAgingBucket> buckets
) {}
