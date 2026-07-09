/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.dto
 * File              : PaymentRunResponse.java
 * Purpose           : Data Transfer Object for request/response in Paymentrun Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentRunController
 * Related Service   : PaymentRunService, PaymentRunServiceImpl
 * Related Repository: PaymentRunRepository
 * Related Entity    : PaymentRun
 * Related DTO       : PaymentRunInvoiceResponse, PaymentRunResponse, PaymentRunSupplierResultResponse
 * Related Mapper    : PaymentRunMapper
 * Related DB Table  : payment_runs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentRunController, PaymentRunService, PaymentRunServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Paymentrun Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.paymentrun.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Paymentrun Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentRunResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.paymentrun.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Paymentrun Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record PaymentRunResponse(
        Long id,
        String runNumber,
        Long companyId,
        String status,
        LocalDate paymentDate,
        String paymentMethod,
        String currencyCode,
        LocalDate filterDueDate,
        Long filterSupplierId,
        String bankAccountCode,
        BigDecimal totalAmount,
        String exportFormat,
        String exportFileName,
        String exportStoragePath,
        String exportChecksum,
        LocalDateTime exportGeneratedAt,
        UUID clientReferenceId,
        
        Integer successfulPaymentsCount,
        Integer failedPaymentsCount,
        Integer processedInvoicesCount,
        String failureReason,
        
        String approvedByEmail,
        LocalDateTime approvedAt,
        String executedByEmail,
        LocalDateTime executedAt,
        String cancelledByEmail,
        LocalDateTime cancelledAt,
        
        String createdByEmail,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        List<PaymentRunInvoiceResponse> invoices,
        List<PaymentRunSupplierResultResponse> supplierResults
) {}

