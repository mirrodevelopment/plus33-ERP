package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetDriverRepository extends JpaRepository<BudgetDriver, Long> {
    Optional<BudgetDriver> findByCompanyIdAndFiscalYearIdAndCode(Long companyId, Long fiscalYearId, String code);
    List<BudgetDriver> findAllByCompanyIdAndFiscalYearId(Long companyId, Long fiscalYearId);
}
