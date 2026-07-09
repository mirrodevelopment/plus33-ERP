/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.dto
 * File              : BankAccountDtos.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BankAccountDtosController
 * Related Service   : BankAccountDtosService, BankAccountDtosServiceImpl
 * Related Repository: BankAccountDtosRepository
 * Related Entity    : BankAccountDtos
 * Related DTO       : BankAccountRequest, BankAccountResponse, BankBranchRequest, BankBranchResponse, BankRequest
 * Related Mapper    : BankAccountDtosMapper
 * Related DB Table  : bank_account_dtoss
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
import java.time.LocalDateTime;

public final class BankAccountDtos {

    private BankAccountDtos() {}

    public record BankRequest(
        String code,
        String name,
        String routingCode,
        String creditRating,
        Integer countryRiskScore,
        BigDecimal exposureLimit,
        BigDecimal maxDepositLimit,
        Boolean active
    ) {}

    public record BankResponse(
        Long id,
        String code,
        String name,
        String routingCode,
        String creditRating,
        Integer countryRiskScore,
        BigDecimal exposureLimit,
        BigDecimal maxDepositLimit,
        BigDecimal currentExposure,
        Boolean active
    ) {}

    public record BankBranchRequest(
        Long bankId,
        String code,
        String name,
        String swiftCode,
        String address
    ) {}

    public record BankBranchResponse(
        Long id,
        Long bankId,
        String bankName,
        String code,
        String name,
        String swiftCode,
        String address
    ) {}

    public record BankAccountRequest(
        Long companyId,
        Long branchId,
        String accountName,
        String accountNumber,
        String iban,
        String currencyCode,
        Long glAccountId,
        String accountType,
        BigDecimal openingBalance
    ) {}

    public record BankAccountResponse(
        Long id,
        Long companyId,
        String companyName,
        Long branchId,
        String bankName,
        String branchName,
        String swiftCode,
        String accountName,
        String accountNumber,
        String iban,
        String currencyCode,
        Long glAccountId,
        String glAccountCode,
        String accountType,
        BigDecimal openingBalance,
        BigDecimal currentBalance,
        BigDecimal reconciledBalance,
        BigDecimal projectedClosingBalance,
        Boolean active,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
    ) {}

    public record BankVirtualAccountRequest(
        Long parentAccountId,
        String virtualAccountNumber,
        String ownerReferenceType,
        Long ownerReferenceId
    ) {}

    public record BankVirtualAccountResponse(
        Long id,
        Long parentAccountId,
        String parentAccountNumber,
        String virtualAccountNumber,
        String ownerReferenceType,
        Long ownerReferenceId,
        Boolean active
    ) {}
}
