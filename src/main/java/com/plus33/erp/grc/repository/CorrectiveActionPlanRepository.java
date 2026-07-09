/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.repository
 * File              : CorrectiveActionPlanRepository.java
 * Purpose           : JPA Repository providing database CRUD for Grc Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CorrectiveActionPlanController
 * Related Service   : CorrectiveActionPlanService, CorrectiveActionPlanServiceImpl
 * Related Repository: CorrectiveActionPlanRepository
 * Related Entity    : CorrectiveActionPlan
 * Related DTO       : N/A
 * Related Mapper    : CorrectiveActionPlanMapper
 * Related DB Table  : corrective_action_plans
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CorrectiveActionPlanService, CorrectiveActionPlanServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Grc Module against the 'corrective_action_plans' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CorrectiveActionPlanRepository extends JpaRepository<CorrectiveActionPlan, Long> {
    List<CorrectiveActionPlan> findByIssueId(Long issueId);
    List<CorrectiveActionPlan> findByStatus(String status);
    long countByStatus(String status);
}
