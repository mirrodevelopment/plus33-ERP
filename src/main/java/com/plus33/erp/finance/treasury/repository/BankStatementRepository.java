package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.BankStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankStatementRepository extends JpaRepository<BankStatement, Long> {
    List<BankStatement> findByBankAccountCompanyId(Long companyId);
    List<BankStatement> findByBankAccountId(Long bankAccountId);
    Optional<BankStatement> findByBankAccountIdAndStatementNumber(Long bankAccountId, String statementNumber);
}
