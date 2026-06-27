package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetPolicyRepository extends JpaRepository<BudgetPolicy, Long> {
    Optional<BudgetPolicy> findByCompanyIdAndCode(Long companyId, String code);
}
