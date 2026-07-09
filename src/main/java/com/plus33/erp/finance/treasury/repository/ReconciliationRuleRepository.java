/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : ReconciliationRuleRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReconciliationRuleController
 * Related Service   : ReconciliationRuleService, ReconciliationRuleServiceImpl
 * Related Repository: ReconciliationRuleRepository
 * Related Entity    : ReconciliationRule
 * Related DTO       : N/A
 * Related Mapper    : ReconciliationRuleMapper
 * Related DB Table  : reconciliation_rules
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ReconciliationRuleService, ReconciliationRuleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'reconciliation_rules' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.ReconciliationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code ReconciliationRuleRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'reconciliation_rules' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code reconciliation_rules}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface ReconciliationRuleRepository extends JpaRepository<ReconciliationRule, Long> {
    List<ReconciliationRule> findByCompanyId(Long companyId);
    List<ReconciliationRule> findByCompanyIdAndActiveTrue(Long companyId);
}