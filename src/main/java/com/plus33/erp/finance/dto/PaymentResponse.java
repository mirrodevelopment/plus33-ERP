/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.dto
 * File              : PaymentResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentController
 * Related Service   : PaymentService, PaymentServiceImpl
 * Related Repository: PaymentRepository
 * Related Entity    : Payment
 * Related DTO       : PaymentAllocationResponse, PaymentResponse
 * Related Mapper    : PaymentMapper
 * Related DB Table  : payments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentController, PaymentService, PaymentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.dto;

import com.plus33.erp.finance.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.dto}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    private Long id;
    private String paymentNumber;
    private Long companyId;
    private String companyName;
    private Long supplierId;
    private String supplierName;
    private Long customerId;
    private String customerName;
    private LocalDate paymentDate;
    private String paymentMethod;
    private String paymentType;
    private BigDecimal amount;
    private String referenceNumber;
    private Long journalEntryId;
    private String journalEntryNumber;
    private String currencyCode;
    private PaymentStatus status;
    private LocalDateTime cancelledAt;
    private Long cancelledByUserId;
    private String cancelledByUserName;
    private String cancellationReason;
    private Long createdByUserId;
    private String createdByUserName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PaymentAllocationResponse> allocations;
}