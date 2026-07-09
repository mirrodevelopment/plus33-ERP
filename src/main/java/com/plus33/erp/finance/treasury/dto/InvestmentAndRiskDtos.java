/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.dto
 * File              : InvestmentAndRiskDtos.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InvestmentAndRiskDtosController
 * Related Service   : InvestmentAndRiskDtosService, InvestmentAndRiskDtosServiceImpl
 * Related Repository: InvestmentAndRiskDtosRepository
 * Related Entity    : InvestmentAndRiskDtos
 * Related DTO       : CashPositionSnapshotLineResponse, CashPositionSnapshotRequest, CashPositionSnapshotResponse, TreasuryComplianceLogRequest, TreasuryComplianceLogResponse
 * Related Mapper    : InvestmentAndRiskDtosMapper
 * Related DB Table  : investment_and_risk_dtoss
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
 * <p><b>Class  :</b> {@code InvestmentAndRiskDtos}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
