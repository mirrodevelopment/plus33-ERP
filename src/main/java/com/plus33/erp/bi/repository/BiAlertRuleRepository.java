/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiAlertRuleRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiAlertRuleController
 * Related Service   : BiAlertRuleService, BiAlertRuleServiceImpl
 * Related Repository: BiAlertRuleRepository
 * Related Entity    : BiAlertRule
 * Related DTO       : N/A
 * Related Mapper    : BiAlertRuleMapper
 * Related DB Table  : bi_alert_rules
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiAlertRuleService, BiAlertRuleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_alert_rules' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiAlertRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiAlertRuleRepository extends JpaRepository<BiAlertRule, Long> {
    java.util.List<BiAlertRule> findByIsActiveTrue();
}
