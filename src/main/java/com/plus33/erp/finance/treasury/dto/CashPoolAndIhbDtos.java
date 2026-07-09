/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.dto
 * File              : CashPoolAndIhbDtos.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CashPoolAndIhbDtosController
 * Related Service   : CashPoolAndIhbDtosService, CashPoolAndIhbDtosServiceImpl
 * Related Repository: CashPoolAndIhbDtosRepository
 * Related Entity    : CashPoolAndIhbDtos
 * Related DTO       : CashPoolMemberRequest, CashPoolMemberResponse, CashPoolRequest, CashPoolResponse, InHouseBankAccountRequest
 * Related Mapper    : CashPoolAndIhbDtosMapper
 * Related DB Table  : cash_pool_and_ihb_dtoss
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
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code CashPoolAndIhbDtos}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public final class CashPoolAndIhbDtos {

    private CashPoolAndIhbDtos() {}

    public record InHouseBankAccountRequest(
        Long subsidiaryCompanyId,
        String accountNumber,
        String currencyCode,
        Long glAccountId
    ) {}

    public record InHouseBankAccountResponse(
        Long id,
        Long subsidiaryCompanyId,
        String subsidiaryCompanyName,
        String accountNumber,
        String currencyCode,
        BigDecimal currentBalance,
        Long glAccountId,
        Boolean active
    ) {}

    public record IntercompanyLoanRequest(
        Long lenderCompanyId,
        Long borrowerCompanyId,
        BigDecimal principalAmount,
        BigDecimal interestRate,
        LocalDate startDate,
        LocalDate maturityDate
    ) {}

    public record IntercompanyLoanResponse(
        Long id,
        Long lenderCompanyId,
        String lenderCompanyName,
        Long borrowerCompanyId,
        String borrowerCompanyName,
        BigDecimal principalAmount,
        BigDecimal interestRate,
        LocalDate startDate,
        LocalDate maturityDate,
        BigDecimal interestAccrued,
        String status
    ) {}

    public record InternalSettlementRequest(
        Long companyId,
        Long sourceIhbAccountId,
        Long targetIhbAccountId,
        BigDecimal amount,
        LocalDate settlementDate,
        String referenceNumber,
        String notes
    ) {}

    public record InternalSettlementResponse(
        Long id,
        Long companyId,
        Long sourceIhbAccountId,
        String sourceCompanyName,
        Long targetIhbAccountId,
        String targetCompanyName,
        BigDecimal amount,
        LocalDate settlementDate,
        String referenceNumber,
        String notes
    ) {}

    public record CashPoolRequest(
        Long companyId,
        String poolName,
        String poolType,
        Long headerAccountId,
        BigDecimal targetBalance,
        List<CashPoolMemberRequest> members
    ) {}

    public record CashPoolMemberRequest(
        Long bankAccountId,
        Integer sweepPriority
    ) {}

    public record CashPoolResponse(
        Long id,
        Long companyId,
        String poolName,
        String poolType,
        Long headerAccountId,
        String headerAccountNumber,
        BigDecimal targetBalance,
        Boolean active,
        List<CashPoolMemberResponse> members
    ) {}

    public record CashPoolMemberResponse(
        Long id,
        Long poolId,
        Long bankAccountId,
        String bankAccountName,
        String bankAccountNumber,
        Integer sweepPriority,
        Boolean active
    ) {}
}
