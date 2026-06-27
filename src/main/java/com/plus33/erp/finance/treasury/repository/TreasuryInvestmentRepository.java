package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.TreasuryInvestment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TreasuryInvestmentRepository extends JpaRepository<TreasuryInvestment, Long> {
    List<TreasuryInvestment> findByBankAccountCompanyId(Long companyId);
    Optional<TreasuryInvestment> findByReferenceNumber(String referenceNumber);
    List<TreasuryInvestment> findByStatus(String status);
}
