package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.BankFeeRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankFeeRuleRepository extends JpaRepository<BankFeeRule, Long> {
    List<BankFeeRule> findByBankAccountId(Long bankAccountId);
    List<BankFeeRule> findByBankAccountIdAndActiveTrue(Long bankAccountId);
}
