/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : SodRuleRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SodRuleController
 * Related Service   : SodRuleService, SodRuleServiceImpl
 * Related Repository: SodRuleRepository
 * Related Entity    : SodRule
 * Related DTO       : N/A
 * Related Mapper    : SodRuleMapper
 * Related DB Table  : sod_rules
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SodRuleService, SodRuleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'sod_rules' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SodRuleRepository extends JpaRepository<SodRule, Long> {
    List<SodRule> findByCompanyId(Long companyId);
    List<SodRule> findByCompanyIdAndSodType(Long companyId, String sodType);
}
