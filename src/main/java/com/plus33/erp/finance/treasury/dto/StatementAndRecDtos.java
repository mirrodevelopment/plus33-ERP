package com.plus33.erp.finance.treasury.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public final class StatementAndRecDtos {

    private StatementAndRecDtos() {}

    public record BankStatementRequest(
        Long bankAccountId,
        String statementNumber,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal openingBalance,
        BigDecimal closingBalance,
        List<BankStatementLineRequest> lines
    ) {}

    public record BankStatementLineRequest(
        LocalDate transactionDate,
        LocalDate valueDate,
        String description,
        String reference,
        BigDecimal amount
    ) {}

    public record BankStatementResponse(
        Long id,
        Long bankAccountId,
        String bankAccountNumber,
        String statementNumber,
        LocalDate startDate,
        LocalDate endDate,
        BigDecimal openingBalance,
        BigDecimal closingBalance,
        String status,
        LocalDateTime importedAt,
        List<BankStatementLineResponse> lines
    ) {}

    public record BankStatementLineResponse(
        Long id,
        Long statementId,
        LocalDate transactionDate,
        LocalDate valueDate,
        String description,
        String reference,
        BigDecimal amount,
        Boolean reconciled,
        Long paymentId,
        String paymentNumber,
        LocalDateTime reconciledAt
    ) {}

    public record ReconciliationRuleRequest(
        Long companyId,
        String ruleName,
        Integer dateToleranceDays,
        String referenceMatchType,
        Boolean allowManyToOne,
        Boolean allowOneToMany,
        Boolean allowSplits
    ) {}

    public record ReconciliationRuleResponse(
        Long id,
        Long companyId,
        String ruleName,
        Integer dateToleranceDays,
        String referenceMatchType,
        Boolean allowManyToOne,
        Boolean allowOneToMany,
        Boolean allowSplits,
        Boolean active
    ) {}

    public record BankFeeRuleRequest(
        Long bankAccountId,
        String chargeType,
        BigDecimal ratePercent,
        BigDecimal fixedAmount,
        Long glExpenseAccountId
    ) {}

    public record BankFeeRuleResponse(
        Long id,
        Long bankAccountId,
        String bankAccountNumber,
        String chargeType,
        BigDecimal ratePercent,
        BigDecimal fixedAmount,
        Long glExpenseAccountId,
        String glExpenseAccountCode,
        Boolean active
    ) {}
}
