package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.dto.BankAccountDtos.*;
import com.plus33.erp.finance.treasury.dto.CashPoolAndIhbDtos.*;

import java.util.List;

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
