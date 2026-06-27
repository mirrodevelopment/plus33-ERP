package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.Budget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, Long>, JpaSpecificationExecutor<Budget> {
    Optional<Budget> findByCompanyIdAndFiscalYearIdAndCodeAndIsForecast(Long companyId, Long fiscalYearId, String code, Boolean isForecast);
}
