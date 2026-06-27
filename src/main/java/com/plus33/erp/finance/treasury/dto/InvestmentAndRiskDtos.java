package com.plus33.erp.finance.treasury.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class InvestmentAndRiskDtos {

    private InvestmentAndRiskDtos() {}

    public record TreasuryInvestmentRequest(
        Long bankAccountId,
        String referenceNumber,
        String investmentType,
        BigDecimal principalAmount,
        BigDecimal interestRate,
        BigDecimal expectedYield,
        BigDecimal effectiveYield,
        BigDecimal compoundedInterest,
        BigDecimal taxWithholding,
        BigDecimal earlyLiquidationPenalty,
        LocalDate startDate,
        LocalDate maturityDate
    ) {}

    public record TreasuryInvestmentResponse(
        Long id,
        Long bankAccountId,
        String bankAccountNumber,
        String referenceNumber,
        String investmentType,
        BigDecimal principalAmount,
        BigDecimal interestRate,
        BigDecimal expectedYield,
        BigDecimal effectiveYield,
        BigDecimal compoundedInterest,
        BigDecimal taxWithholding,
        BigDecimal earlyLiquidationPenalty,
        LocalDate startDate,
        LocalDate maturityDate,
        BigDecimal interestEarned,
        BigDecimal accruedInterest,
        String status,
        LocalDateTime createdAt
    ) {}

    public record TreasuryLimitRequest(
        Long companyId,
        String limitType,
        String currencyCode,
        String countryCode,
        Long targetBankId,
        BigDecimal limitAmount,
        BigDecimal warningThresholdPercent
    ) {}

    public record TreasuryLimitResponse(
        Long id,
        Long companyId,
        String limitType,
        String currencyCode,
        String countryCode,
        Long targetBankId,
        String targetBankName,
        BigDecimal limitAmount,
        BigDecimal warningThresholdPercent,
        Boolean active
    ) {}

    public record CashPositionSnapshotRequest(
        Long companyId,
        String snapshotType
    ) {}

    public record CashPositionSnapshotResponse(
        Long id,
        Long companyId,
        LocalDateTime snapshotDate,
        String snapshotType,
        BigDecimal totalCashUsd,
        String createdBy,
        List<CashPositionSnapshotLineResponse> lines
    ) {}

    public record CashPositionSnapshotLineResponse(
        Long id,
        Long bankAccountId,
        String bankAccountNumber,
        BigDecimal currentBalance,
        BigDecimal reconciledBalance,
        String currencyCode
    ) {}

    public record TreasuryComplianceLogRequest(
        Long companyId,
        String actionType,
        String details,
        String performedBy
    ) {}

    public record TreasuryComplianceLogResponse(
        Long id,
        Long companyId,
        String actionType,
        String details,
        String performedBy,
        LocalDateTime timestamp
    ) {}
}
