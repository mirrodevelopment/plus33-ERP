package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetTemplateRepository extends JpaRepository<BudgetTemplate, Long> {
    Optional<BudgetTemplate> findByCode(String code);
}
