package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.BankBranch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankBranchRepository extends JpaRepository<BankBranch, Long> {
    List<BankBranch> findByBankId(Long bankId);
    Optional<BankBranch> findByBankIdAndCode(Long bankId, String code);
}
