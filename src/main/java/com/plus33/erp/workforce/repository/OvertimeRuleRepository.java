/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : OvertimeRuleRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: OvertimeRuleController
 * Related Service   : OvertimeRuleService, OvertimeRuleServiceImpl
 * Related Repository: OvertimeRuleRepository
 * Related Entity    : OvertimeRule
 * Related DTO       : N/A
 * Related Mapper    : OvertimeRuleMapper
 * Related DB Table  : overtime_rules
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : OvertimeRuleService, OvertimeRuleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'overtime_rules' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.OvertimeRule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OvertimeRuleRepository extends JpaRepository<OvertimeRule, Long> {
}
