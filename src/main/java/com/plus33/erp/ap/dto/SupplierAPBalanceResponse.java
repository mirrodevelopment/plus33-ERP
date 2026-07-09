/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ap Module
 * Package           : com.plus33.erp.ap.dto
 * File              : SupplierAPBalanceResponse.java
 * Purpose           : Data Transfer Object for request/response in Ap Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierAPBalanceController
 * Related Service   : SupplierAPBalanceService, SupplierAPBalanceServiceImpl
 * Related Repository: SupplierAPBalanceRepository
 * Related Entity    : SupplierAPBalance
 * Related DTO       : SupplierAPBalanceResponse
 * Related Mapper    : SupplierAPBalanceMapper
 * Related DB Table  : supplier_a_p_balances
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierAPBalanceController, SupplierAPBalanceService, SupplierAPBalanceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Ap Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.ap.dto;

import java.math.BigDecimal;

public record SupplierAPBalanceResponse(
        Long supplierId,
        String supplierName,
        Long companyId,
        BigDecimal totalOutstanding,
        BigDecimal totalOverdue,
        BigDecimal totalPaid,
        BigDecimal totalCredited
) {}
