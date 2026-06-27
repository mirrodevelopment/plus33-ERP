package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetControlCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BudgetControlCacheRepository extends JpaRepository<BudgetControlCache, Long> {
}
