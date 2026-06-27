package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.treasury.dto.BankAccountDtos.*;
import com.plus33.erp.finance.treasury.dto.CashPoolAndIhbDtos.*;
import com.plus33.erp.finance.treasury.entity.*;
import com.plus33.erp.finance.treasury.repository.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankRepository bankRepository;
    private final BankBranchRepository bankBranchRepository;
    private final BankAccountRepository bankAccountRepository;
    private final BankVirtualAccountRepository bankVirtualAccountRepository;
    private final CashPoolRepository cashPoolRepository;
    private final CashPoolMemberRepository cashPoolMemberRepository;
    private final CompanyRepository companyRepository;
    private final AccountRepository accountRepository;

    // Bank operations
    @Override
    @Transactional
    public BankResponse createBank(BankRequest request) {
        if (bankRepository.findByCode(request.code()).isPresent()) {
            throw new BusinessException("Bank code already exists: " + request.code());
        }
        Bank bank = Bank.builder()
            .code(request.code())
            .name(request.name())
            .routingCode(request.routingCode())
            .creditRating(request.creditRating() != null ? request.creditRating() : "A")
            .countryRiskScore(request.countryRiskScore() != null ? request.countryRiskScore() : 1)
            .exposureLimit(request.exposureLimit() != null ? request.exposureLimit() : new BigDecimal("10000000.00"))
            .maxDepositLimit(request.maxDepositLimit() != null ? request.maxDepositLimit() : new BigDecimal("10000000.00"))
            .currentExposure(BigDecimal.ZERO)
            .active(request.active() != null ? request.active() : true)
            .build();
        Bank saved = bankRepository.save(bank);
        return mapToBankResponse(saved);
    }

    @Override
    public BankResponse getBankById(Long id) {
        Bank bank = bankRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bank not found with ID: " + id));
        return mapToBankResponse(bank);
    }

    @Override
    public List<BankResponse> getAllBanks() {
        return bankRepository.findAll().stream().map(this::mapToBankResponse).toList();
    }

    @Override
    @Transactional
    public void updateBankExposure(Long bankId) {
        Bank bank = bankRepository.findById(bankId)
            .orElseThrow(() -> new ResourceNotFoundException("Bank not found with ID: " + bankId));
        
        // Sum current balances of all bank accounts in this bank
        BigDecimal total = bankAccountRepository.findAll().stream()
            .filter(a -> a.getBranch().getBank().getId().equals(bankId) && a.getActive())
            .map(BankAccount::getCurrentBalance)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        bank.setCurrentExposure(total);
        bankRepository.save(bank);
    }

    // Bank Branch operations
    @Override
    @Transactional
    public BankBranchResponse createBranch(BankBranchRequest request) {
        Bank bank = bankRepository.findById(request.bankId())
            .orElseThrow(() -> new ResourceNotFoundException("Bank not found with ID: " + request.bankId()));
        
        if (bankBranchRepository.findByBankIdAndCode(request.bankId(), request.code()).isPresent()) {
            throw new BusinessException("Branch code already exists for this bank: " + request.code());
        }

        BankBranch branch = BankBranch.builder()
            .bank(bank)
            .code(request.code())
            .name(request.name())
            .swiftCode(request.swiftCode())
            .address(request.address())
            .build();
        BankBranch saved = bankBranchRepository.save(branch);
        return mapToBranchResponse(saved);
    }

    @Override
    public BankBranchResponse getBranchById(Long id) {
        BankBranch branch = bankBranchRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Branch not found with ID: " + id));
        return mapToBranchResponse(branch);
    }

    @Override
    public List<BankBranchResponse> getBranchesByBank(Long bankId) {
        return bankBranchRepository.findByBankId(bankId).stream().map(this::mapToBranchResponse).toList();
    }

    // Bank Account operations
    @Override
    @Transactional
    public BankAccountResponse createBankAccount(BankAccountRequest request) {
        Company company = companyRepository.findById(request.companyId())
            .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));
        BankBranch branch = bankBranchRepository.findById(request.branchId())
            .orElseThrow(() -> new ResourceNotFoundException("Branch not found with ID: " + request.branchId()));
        Account glAccount = accountRepository.findById(request.glAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("GL Account not found with ID: " + request.glAccountId()));

        if (bankAccountRepository.findByCompanyIdAndAccountNumber(request.companyId(), request.accountNumber()).isPresent()) {
            throw new BusinessException("Bank Account already exists: " + request.accountNumber());
        }

        BankAccount account = BankAccount.builder()
            .company(company)
            .branch(branch)
            .accountName(request.accountName())
            .accountNumber(request.accountNumber())
            .iban(request.iban())
            .currencyCode(request.currencyCode() != null ? request.currencyCode() : "AED")
            .glAccount(glAccount)
            .accountType(request.accountType() != null ? request.accountType() : "CURRENT")
            .openingBalance(request.openingBalance() != null ? request.openingBalance() : BigDecimal.ZERO)
            .currentBalance(request.openingBalance() != null ? request.openingBalance() : BigDecimal.ZERO)
            .reconciledBalance(request.openingBalance() != null ? request.openingBalance() : BigDecimal.ZERO)
            .projectedClosingBalance(request.openingBalance() != null ? request.openingBalance() : BigDecimal.ZERO)
            .active(true)
            .build();

        BankAccount saved = bankAccountRepository.save(account);
        updateBankExposure(branch.getBank().getId());
        return mapToAccountResponse(saved);
    }

    @Override
    public BankAccountResponse getBankAccountById(Long id) {
        BankAccount account = bankAccountRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Bank Account not found with ID: " + id));
        return mapToAccountResponse(account);
    }

    @Override
    public List<BankAccountResponse> getBankAccountsByCompany(Long companyId) {
        return bankAccountRepository.findByCompanyId(companyId).stream().map(this::mapToAccountResponse).toList();
    }

    @Override
    @Transactional
    public void updateBalances(Long accountId) {
        BankAccount account = bankAccountRepository.findById(accountId)
            .orElseThrow(() -> new ResourceNotFoundException("BankAccount not found with ID: " + accountId));
        updateBankExposure(account.getBranch().getBank().getId());
    }

    // Virtual Account operations
    @Override
    @Transactional
    public BankVirtualAccountResponse createVirtualAccount(BankVirtualAccountRequest request) {
        BankAccount parent = bankAccountRepository.findById(request.parentAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("Parent Bank Account not found with ID: " + request.parentAccountId()));

        if (bankVirtualAccountRepository.findByVirtualAccountNumber(request.virtualAccountNumber()).isPresent()) {
            throw new BusinessException("Virtual Account number already exists: " + request.virtualAccountNumber());
        }

        BankVirtualAccount va = BankVirtualAccount.builder()
            .parentAccount(parent)
            .virtualAccountNumber(request.virtualAccountNumber())
            .ownerReferenceType(request.ownerReferenceType())
            .ownerReferenceId(request.ownerReferenceId())
            .active(true)
            .build();
        BankVirtualAccount saved = bankVirtualAccountRepository.save(va);
        return mapToVaResponse(saved);
    }

    @Override
    public List<BankVirtualAccountResponse> getVirtualAccountsByParent(Long parentAccountId) {
        return bankVirtualAccountRepository.findByParentAccountId(parentAccountId).stream().map(this::mapToVaResponse).toList();
    }

    // Cash Pool operations
    @Override
    @Transactional
    public CashPoolResponse createCashPool(CashPoolRequest request) {
        Company company = companyRepository.findById(request.companyId())
            .orElseThrow(() -> new ResourceNotFoundException("Company not found with ID: " + request.companyId()));
        BankAccount header = bankAccountRepository.findById(request.headerAccountId())
            .orElseThrow(() -> new ResourceNotFoundException("Header Account not found with ID: " + request.headerAccountId()));

        CashPool pool = CashPool.builder()
            .company(company)
            .poolName(request.poolName())
            .poolType(request.poolType() != null ? request.poolType() : "ZERO_BALANCE")
            .headerAccount(header)
            .targetBalance(request.targetBalance() != null ? request.targetBalance() : BigDecimal.ZERO)
            .active(true)
            .build();

        CashPool savedPool = cashPoolRepository.save(pool);

        List<CashPoolMemberResponse> memberResponses = new ArrayList<>();
        if (request.members() != null) {
            for (CashPoolMemberRequest memReq : request.members()) {
                BankAccount memberAcct = bankAccountRepository.findById(memReq.bankAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Member Account not found with ID: " + memReq.bankAccountId()));
                
                CashPoolMember member = CashPoolMember.builder()
                    .pool(savedPool)
                    .bankAccount(memberAcct)
                    .sweepPriority(memReq.sweepPriority() != null ? memReq.sweepPriority() : 1)
                    .active(true)
                    .build();
                
                CashPoolMember savedMem = cashPoolMemberRepository.save(member);
                savedPool.getMembers().add(savedMem);
                memberResponses.add(mapToMemberResponse(savedMem));
            }
        }

        return mapToPoolResponse(savedPool, memberResponses);
    }

    @Override
    public CashPoolResponse getCashPoolById(Long id) {
        CashPool pool = cashPoolRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Cash Pool not found with ID: " + id));
        List<CashPoolMemberResponse> memberResponses = pool.getMembers().stream().map(this::mapToMemberResponse).toList();
        return mapToPoolResponse(pool, memberResponses);
    }

    @Override
    public List<CashPoolResponse> getCashPoolsByCompany(Long companyId) {
        return cashPoolRepository.findByCompanyId(companyId).stream().map(p -> {
            List<CashPoolMemberResponse> memberResponses = p.getMembers().stream().map(this::mapToMemberResponse).toList();
            return mapToPoolResponse(p, memberResponses);
        }).toList();
    }

    @Override
    @Transactional
    public void executeCashPoolSweeps(Long poolId, String username) {
        CashPool pool = cashPoolRepository.findById(poolId)
            .orElseThrow(() -> new ResourceNotFoundException("Cash Pool not found with ID: " + poolId));
        
        if (!pool.getActive()) return;

        BankAccount header = pool.getHeaderAccount();
        BigDecimal headerChange = BigDecimal.ZERO;

        for (CashPoolMember member : pool.getMembers()) {
            if (!member.getActive()) continue;

            BankAccount memberAcct = member.getBankAccount();
            BigDecimal balance = memberAcct.getCurrentBalance();

            if ("ZERO_BALANCE".equals(pool.getPoolType())) {
                if (balance.compareTo(BigDecimal.ZERO) != 0) {
                    // Sweep amount
                    BigDecimal sweepAmount = balance;
                    
                    // Deduct from member account
                    memberAcct.setCurrentBalance(BigDecimal.ZERO);
                    bankAccountRepository.save(memberAcct);

                    // Add to header account (assuming currency conversion is not needed or handled simply as 1:1 for ZBP)
                    headerChange = headerChange.add(sweepAmount);
                    log.info("ZBP SWEEP: Swept {} from member account {} to header account {}", sweepAmount, memberAcct.getAccountNumber(), header.getAccountNumber());
                }
            } else if ("TARGET_BALANCE".equals(pool.getPoolType())) {
                BigDecimal target = pool.getTargetBalance();
                BigDecimal difference = balance.subtract(target);

                if (difference.compareTo(BigDecimal.ZERO) != 0) {
                    // Adjust member to target
                    memberAcct.setCurrentBalance(target);
                    bankAccountRepository.save(memberAcct);

                    headerChange = headerChange.add(difference);
                    log.info("TARGET SWEEP: Swept {} from member account {} to header account {}", difference, memberAcct.getAccountNumber(), header.getAccountNumber());
                }
            }
        }

        if (headerChange.compareTo(BigDecimal.ZERO) != 0) {
            header.setCurrentBalance(header.getCurrentBalance().add(headerChange));
            bankAccountRepository.save(header);
            updateBankExposure(header.getBranch().getBank().getId());
        }
    }

    // Mapping Helpers
    private BankResponse mapToBankResponse(Bank bank) {
        return new BankResponse(
            bank.getId(),
            bank.getCode(),
            bank.getName(),
            bank.getRoutingCode(),
            bank.getCreditRating(),
            bank.getCountryRiskScore(),
            bank.getExposureLimit(),
            bank.getMaxDepositLimit(),
            bank.getCurrentExposure(),
            bank.getActive()
        );
    }

    private BankBranchResponse mapToBranchResponse(BankBranch branch) {
        return new BankBranchResponse(
            branch.getId(),
            branch.getBank().getId(),
            branch.getBank().getName(),
            branch.getCode(),
            branch.getName(),
            branch.getSwiftCode(),
            branch.getAddress()
        );
    }

    private BankAccountResponse mapToAccountResponse(BankAccount account) {
        return new BankAccountResponse(
            account.getId(),
            account.getCompany().getId(),
            account.getCompany().getName(),
            account.getBranch().getId(),
            account.getBranch().getBank().getName(),
            account.getBranch().getName(),
            account.getBranch().getSwiftCode(),
            account.getAccountName(),
            account.getAccountNumber(),
            account.getIban(),
            account.getCurrencyCode(),
            account.getGlAccount().getId(),
            account.getGlAccount().getAccountCode(),
            account.getAccountType(),
            account.getOpeningBalance(),
            account.getCurrentBalance(),
            account.getReconciledBalance(),
            account.getProjectedClosingBalance(),
            account.getActive(),
            account.getCreatedAt(),
            account.getUpdatedAt()
        );
    }

    private BankVirtualAccountResponse mapToVaResponse(BankVirtualAccount va) {
        return new BankVirtualAccountResponse(
            va.getId(),
            va.getParentAccount().getId(),
            va.getParentAccount().getAccountNumber(),
            va.getVirtualAccountNumber(),
            va.getOwnerReferenceType(),
            va.getOwnerReferenceId(),
            va.getActive()
        );
    }

    private CashPoolResponse mapToPoolResponse(CashPool pool, List<CashPoolMemberResponse> members) {
        return new CashPoolResponse(
            pool.getId(),
            pool.getCompany().getId(),
            pool.getPoolName(),
            pool.getPoolType(),
            pool.getHeaderAccount().getId(),
            pool.getHeaderAccount().getAccountNumber(),
            pool.getTargetBalance(),
            pool.getActive(),
            members
        );
    }

    private CashPoolMemberResponse mapToMemberResponse(CashPoolMember member) {
        return new CashPoolMemberResponse(
            member.getId(),
            member.getPool().getId(),
            member.getBankAccount().getId(),
            member.getBankAccount().getAccountName(),
            member.getBankAccount().getAccountNumber(),
            member.getSweepPriority(),
            member.getActive()
        );
    }
}
