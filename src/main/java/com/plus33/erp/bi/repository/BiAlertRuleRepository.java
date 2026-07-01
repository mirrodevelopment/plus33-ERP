package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiAlertRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiAlertRuleRepository extends JpaRepository<BiAlertRule, Long> {
    java.util.List<BiAlertRule> findByIsActiveTrue();
}
