/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ar Module
 * Package           : com.plus33.erp.ar.dto
 * File              : ARStatusBucket.java
 * Purpose           : Component of Ar Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ARStatusBucketController
 * Related Service   : ARStatusBucketService, ARStatusBucketServiceImpl
 * Related Repository: ARStatusBucketRepository
 * Related Entity    : ARStatusBucket
 * Related DTO       : N/A
 * Related Mapper    : ARStatusBucketMapper
 * Related DB Table  : a_r_status_buckets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Ar Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Ar Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.ar.dto;

import java.math.BigDecimal;

/**
 * One bucket from mv_receivables_dashboard (per status).
 */
public record ARStatusBucket(
        String status,
        Long invoiceCount,
        BigDecimal totalAmount,
        BigDecimal outstandingAmount
) {}
