package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiDataQualityRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiDataQualityRuleRepository extends JpaRepository<BiDataQualityRule, Long> {
    java.util.List<BiDataQualityRule> findByIsActiveTrueAndSourceTable(String sourceTable);
}
