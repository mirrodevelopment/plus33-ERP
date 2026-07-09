/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.dto
 * File              : PaymentRunSupplierResultResponse.java
 * Purpose           : Data Transfer Object for request/response in Paymentrun Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentRunSupplierResultController
 * Related Service   : PaymentRunSupplierResultService, PaymentRunSupplierResultServiceImpl
 * Related Repository: PaymentRunSupplierResultRepository
 * Related Entity    : PaymentRunSupplierResult
 * Related DTO       : PaymentRunSupplierResultResponse
 * Related Mapper    : PaymentRunSupplierResultMapper
 * Related DB Table  : payment_run_supplier_results
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentRunSupplierResultController, PaymentRunSupplierResultService, PaymentRunSupplierResultServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Paymentrun Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.paymentrun.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentRunSupplierResultResponse(
        Long id,
        Long supplierId,
        String supplierName,
        Long paymentId,
        String paymentNumber,
        String status,
        BigDecimal amount,
        String errorMessage,
        LocalDateTime startedAt,
        LocalDateTime completedAt
) {}
