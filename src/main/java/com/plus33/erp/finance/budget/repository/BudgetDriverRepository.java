/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.repository
 * File              : BudgetDriverRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetDriverController
 * Related Service   : BudgetDriverService, BudgetDriverServiceImpl
 * Related Repository: BudgetDriverRepository
 * Related Entity    : BudgetDriver
 * Related DTO       : N/A
 * Related Mapper    : BudgetDriverMapper
 * Related DB Table  : budget_drivers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetDriverService, BudgetDriverServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'budget_drivers' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.BudgetDriver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetDriverRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'budget_drivers' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_drivers}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BudgetDriverRepository extends JpaRepository<BudgetDriver, Long> {
    Optional<BudgetDriver> findByCompanyIdAndFiscalYearIdAndCode(Long companyId, Long fiscalYearId, String code);
    List<BudgetDriver> findAllByCompanyIdAndFiscalYearId(Long companyId, Long fiscalYearId);
}