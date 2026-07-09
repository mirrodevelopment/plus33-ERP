/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ar Module
 * Package           : com.plus33.erp.ar.dto
 * File              : CustomerARBalanceResponse.java
 * Purpose           : Data Transfer Object for request/response in Ar Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerARBalanceController
 * Related Service   : CustomerARBalanceService, CustomerARBalanceServiceImpl
 * Related Repository: CustomerARBalanceRepository
 * Related Entity    : CustomerARBalance
 * Related DTO       : CustomerARBalanceResponse
 * Related Mapper    : CustomerARBalanceMapper
 * Related DB Table  : customer_a_r_balances
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CustomerARBalanceController, CustomerARBalanceService, CustomerARBalanceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Ar Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.ar.dto;

import java.math.BigDecimal;

/**
 * Current AR position for a single customer.
 */
public record CustomerARBalanceResponse(
        Long customerId,
        String customerName,
        Long companyId,
        BigDecimal creditLimit,
        BigDecimal totalOutstanding,
        BigDecimal totalOverdue,
        BigDecimal totalPaid,
        BigDecimal totalCredited
) {}
