package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetVersionRepository extends JpaRepository<BudgetVersion, Long> {
    Optional<BudgetVersion> findByBudgetIdAndVersionCode(Long budgetId, String versionCode);
    List<BudgetVersion> findAllByBudgetId(Long budgetId);
}
