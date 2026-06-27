package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetWorkflowTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetWorkflowTemplateRepository extends JpaRepository<BudgetWorkflowTemplate, Long> {
    Optional<BudgetWorkflowTemplate> findByCompanyIdAndCode(Long companyId, String code);
}
