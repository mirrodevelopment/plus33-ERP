/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.service
 * File              : BankAccountService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BankAccountController
 * Related Service   : BankAccountService, BankAccountServiceImpl
 * Related Repository: BankAccountRepository
 * Related Entity    : BankAccount
 * Related DTO       : BankAccountRequest, BankAccountResponse, BankBranchRequest, BankBranchResponse, BankRequest
 * Related Mapper    : BankAccountMapper
 * Related DB Table  : bank_accounts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.dto.BankAccountDtos.*;
import com.plus33.erp.finance.treasury.dto.CashPoolAndIhbDtos.*;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BankAccountService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface BankAccountService {
    // Bank operations
    BankResponse createBank(BankRequest request);
    BankResponse getBankById(Long id);
    List<BankResponse> getAllBanks();
    void updateBankExposure(Long bankId);

    // Bank Branch operations
    BankBranchResponse createBranch(BankBranchRequest request);
    BankBranchResponse getBranchById(Long id);
    List<BankBranchResponse> getBranchesByBank(Long bankId);

    // Bank Account operations
    BankAccountResponse createBankAccount(BankAccountRequest request);
    BankAccountResponse getBankAccountById(Long id);
    List<BankAccountResponse> getBankAccountsByCompany(Long companyId);
    void updateBalances(Long accountId);

    // Virtual Account operations
    BankVirtualAccountResponse createVirtualAccount(BankVirtualAccountRequest request);
    List<BankVirtualAccountResponse> getVirtualAccountsByParent(Long parentAccountId);

    // Cash Pool operations
    CashPoolResponse createCashPool(CashPoolRequest request);
    CashPoolResponse getCashPoolById(Long id);
    List<CashPoolResponse> getCashPoolsByCompany(Long companyId);
    void executeCashPoolSweeps(Long poolId, String username);
}
