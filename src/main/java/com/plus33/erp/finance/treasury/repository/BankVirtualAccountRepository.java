package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.BankVirtualAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankVirtualAccountRepository extends JpaRepository<BankVirtualAccount, Long> {
    Optional<BankVirtualAccount> findByVirtualAccountNumber(String virtualAccountNumber);
    List<BankVirtualAccount> findByParentAccountId(Long parentAccountId);
}
