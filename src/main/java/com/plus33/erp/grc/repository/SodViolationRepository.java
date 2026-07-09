/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : SodViolationRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SodViolationController
 * Related Service   : SodViolationService, SodViolationServiceImpl
 * Related Repository: SodViolationRepository
 * Related Entity    : SodViolation
 * Related DTO       : N/A
 * Related Mapper    : SodViolationMapper
 * Related DB Table  : sod_violations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SodViolationService, SodViolationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'sod_violations' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SodViolationRepository extends JpaRepository<SodViolation, Long> {
    List<SodViolation> findBySodRuleId(Long ruleId);
    List<SodViolation> findByUserId(Long userId);
    List<SodViolation> findByStatus(String status);
    long countByStatus(String status);
}
