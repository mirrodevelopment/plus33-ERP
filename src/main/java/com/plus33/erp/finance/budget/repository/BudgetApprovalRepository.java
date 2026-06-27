package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BudgetApprovalRepository extends JpaRepository<BudgetApproval, Long> {
    List<BudgetApproval> findAllByBudgetIdOrderByApprovalStepAsc(Long budgetId);
    Optional<BudgetApproval> findByBudgetIdAndApprovalStep(Long budgetId, Integer approvalStep);
}
