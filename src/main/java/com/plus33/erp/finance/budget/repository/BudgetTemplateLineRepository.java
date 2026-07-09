/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.repository
 * File              : BudgetTemplateLineRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetTemplateLineController
 * Related Service   : BudgetTemplateLineService, BudgetTemplateLineServiceImpl
 * Related Repository: BudgetTemplateLineRepository
 * Related Entity    : BudgetTemplateLine
 * Related DTO       : N/A
 * Related Mapper    : BudgetTemplateLineMapper
 * Related DB Table  : budget_template_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetTemplateLineService, BudgetTemplateLineServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'budget_template_lines' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetTemplateLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetTemplateLineRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'budget_template_lines' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_template_lines}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BudgetTemplateLineRepository extends JpaRepository<BudgetTemplateLine, Long> {
    List<BudgetTemplateLine> findAllByTemplateId(Long templateId);
}