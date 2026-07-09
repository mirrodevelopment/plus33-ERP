/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.dto
 * File              : PaymentFactoryDtos.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentFactoryDtosController
 * Related Service   : PaymentFactoryDtosService, PaymentFactoryDtosServiceImpl
 * Related Repository: PaymentFactoryDtosRepository
 * Related Entity    : PaymentFactoryDtos
 * Related DTO       : CashTransferRequest, CashTransferResponse, PaymentBatchRequest, PaymentBatchResponse, PaymentFileRequest
 * Related Mapper    : PaymentFactoryDtosMapper
 * Related DB Table  : payment_factory_dtoss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentFactoryDtos}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public final class PaymentFactoryDtos {

    private PaymentFactoryDtos() {}

    public record PaymentBatchRequest(
        Long companyId,
        String batchNumber,
        Long sourceBankAccountId,
        List<Long> paymentIds
    ) {}

    public record PaymentBatchResponse(
        Long id,
        Long companyId,
        String batchNumber,
        Long sourceBankAccountId,
        String sourceBankAccountNumber,
        String status,
        BigDecimal totalAmount,
        String currencyCode,
        String createdBy,
        String approvedBy,
        LocalDateTime createdAt
    ) {}

    public record PaymentFileRequest(
        Long batchId,
        String fileFormat
    ) {}

    public record PaymentFileResponse(
        Long id,
        Long batchId,
        String fileName,
        String fileFormat,
        String fileContent,
        LocalDateTime generatedAt
    ) {}

    public record PaymentTransmissionLogRequest(
        Long fileId,
        String transmissionMethod,
        String status,
        String requestPayload,
        String responsePayload,
        String errorMessage
    ) {}

    public record PaymentTransmissionLogResponse(
        Long id,
        Long fileId,
        String transmissionMethod,
        String status,
        String requestPayload,
        String responsePayload,
        String errorMessage,
        LocalDateTime transmittedAt
    ) {}

    public record CashTransferRequest(
        Long companyId,
        Long sourceBankAccountId,
        Long destinationBankAccountId,
        LocalDate transferDate,
        BigDecimal amount,
        BigDecimal exchangeRate,
        BigDecimal fees,
        String referenceNumber
    ) {}

    public record CashTransferResponse(
        Long id,
        Long companyId,
        Long sourceBankAccountId,
        String sourceBankAccountNumber,
        Long destinationBankAccountId,
        String destinationBankAccountNumber,
        LocalDate transferDate,
        BigDecimal amount,
        BigDecimal exchangeRate,
        BigDecimal fees,
        String referenceNumber,
        String status,
        String createdBy,
        String approvedBy,
        LocalDateTime approvedAt,
        LocalDateTime createdAt
    ) {}

    public record TreasuryApprovalStepRequest(
        Integer stepSequence,
        String roleCode,
        BigDecimal minAmount,
        BigDecimal maxAmount
    ) {}

    public record TreasuryApprovalStepResponse(
        Long id,
        Integer stepSequence,
        String roleCode,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        Boolean active
    ) {}

    public record TreasuryApprovalRequest(
        Long transferId,
        Long paymentBatchId,
        String remarks
    ) {}

    public record TreasuryApprovalResponse(
        Long id,
        Integer approvalStep,
        String roleCode,
        Long transferId,
        Long paymentBatchId,
        String status,
        String approverUsername,
        LocalDateTime approvedAt,
        String remarks
    ) {}
}
