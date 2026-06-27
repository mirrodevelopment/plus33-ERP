package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetRevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetRevisionRepository extends JpaRepository<BudgetRevision, Long> {
    List<BudgetRevision> findAllByBudgetId(Long budgetId);
    List<BudgetRevision> findAllByBudgetLineId(Long budgetLineId);
}
