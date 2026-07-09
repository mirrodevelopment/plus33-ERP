/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiDataQualityRuleRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiDataQualityRuleController
 * Related Service   : BiDataQualityRuleService, BiDataQualityRuleServiceImpl
 * Related Repository: BiDataQualityRuleRepository
 * Related Entity    : BiDataQualityRule
 * Related DTO       : N/A
 * Related Mapper    : BiDataQualityRuleMapper
 * Related DB Table  : bi_data_quality_rules
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiDataQualityRuleService, BiDataQualityRuleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_data_quality_rules' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiDataQualityRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiDataQualityRuleRepository extends JpaRepository<BiDataQualityRule, Long> {
    java.util.List<BiDataQualityRule> findByIsActiveTrueAndSourceTable(String sourceTable);
}
