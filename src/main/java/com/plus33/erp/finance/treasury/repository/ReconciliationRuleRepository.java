package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.ReconciliationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReconciliationRuleRepository extends JpaRepository<ReconciliationRule, Long> {
    List<ReconciliationRule> findByCompanyId(Long companyId);
    List<ReconciliationRule> findByCompanyIdAndActiveTrue(Long companyId);
}
