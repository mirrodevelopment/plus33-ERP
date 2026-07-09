/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.repository
 * File              : BudgetTemplateRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetTemplateController
 * Related Service   : BudgetTemplateService, BudgetTemplateServiceImpl
 * Related Repository: BudgetTemplateRepository
 * Related Entity    : BudgetTemplate
 * Related DTO       : N/A
 * Related Mapper    : BudgetTemplateMapper
 * Related DB Table  : budget_templates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetTemplateService, BudgetTemplateServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'budget_templates' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetTemplateRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'budget_templates' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_templates}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BudgetTemplateRepository extends JpaRepository<BudgetTemplate, Long> {
    Optional<BudgetTemplate> findByCode(String code);
}