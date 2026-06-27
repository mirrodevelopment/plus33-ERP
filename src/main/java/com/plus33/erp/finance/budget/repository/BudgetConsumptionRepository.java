package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetConsumption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetConsumptionRepository extends JpaRepository<BudgetConsumption, Long> {
    Optional<BudgetConsumption> findBySourceModuleAndSourceReferenceId(String sourceModule, Long sourceReferenceId);
    List<BudgetConsumption> findAllByBudgetLineId(Long budgetLineId);
}
