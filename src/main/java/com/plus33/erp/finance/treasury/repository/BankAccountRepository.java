package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    List<BankAccount> findByCompanyId(Long companyId);
    Optional<BankAccount> findByCompanyIdAndAccountNumber(Long companyId, String accountNumber);
    Optional<BankAccount> findByGlAccountId(Long glAccountId);
}
