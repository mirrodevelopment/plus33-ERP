package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.BankStatementLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankStatementLineRepository extends JpaRepository<BankStatementLine, Long> {
    List<BankStatementLine> findByStatementId(Long statementId);
    List<BankStatementLine> findByStatementIdAndReconciled(Long statementId, Boolean reconciled);
}
