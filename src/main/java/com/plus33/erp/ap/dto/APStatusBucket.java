/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ap Module
 * Package           : com.plus33.erp.ap.dto
 * File              : APStatusBucket.java
 * Purpose           : Component of Ap Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: APStatusBucketController
 * Related Service   : APStatusBucketService, APStatusBucketServiceImpl
 * Related Repository: APStatusBucketRepository
 * Related Entity    : APStatusBucket
 * Related DTO       : N/A
 * Related Mapper    : APStatusBucketMapper
 * Related DB Table  : a_p_status_buckets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Ap Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Ap Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.ap.dto;

import java.math.BigDecimal;

public record APStatusBucket(
        String status,
        Long billCount,
        BigDecimal totalAmount,
        BigDecimal outstandingAmount
) {}
