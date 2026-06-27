package com.plus33.erp.finance.treasury.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

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
