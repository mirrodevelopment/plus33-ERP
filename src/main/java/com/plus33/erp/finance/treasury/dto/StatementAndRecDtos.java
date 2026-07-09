/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.dto
 * File              : StatementAndRecDtos.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StatementAndRecDtosController
 * Related Service   : StatementAndRecDtosService, StatementAndRecDtosServiceImpl
 * Related Repository: StatementAndRecDtosRepository
 * Related Entity    : StatementAndRecDtos
 * Related DTO       : BankFeeRuleRequest, BankFeeRuleResponse, BankStatementLineRequest, BankStatementLineResponse, BankStatementRequest
 * Related Mapper    : StatementAndRecDtosMapper
 * Related DB Table  : statement_and_rec_dtoss
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
 * <p><b>Class  :</b> {@code StatementAndRecDtos}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
