/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : PayrollItemBreakdownRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollItemBreakdownController
 * Related Service   : PayrollItemBreakdownService, PayrollItemBreakdownServiceImpl
 * Related Repository: PayrollItemBreakdownRepository
 * Related Entity    : PayrollItemBreakdown
 * Related DTO       : N/A
 * Related Mapper    : PayrollItemBreakdownMapper
 * Related DB Table  : payroll_item_breakdowns
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollItemBreakdownService, PayrollItemBreakdownServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'payroll_item_breakdowns' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollItemBreakdown;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollItemBreakdownRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'payroll_item_breakdowns' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code payroll_item_breakdowns}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PayrollItemBreakdownRepository extends JpaRepository<PayrollItemBreakdown, Long> {
    List<PayrollItemBreakdown> findByPayrollRunItemId(Long payrollRunItemId);
}