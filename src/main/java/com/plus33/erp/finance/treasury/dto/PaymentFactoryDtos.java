package com.plus33.erp.finance.treasury.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
