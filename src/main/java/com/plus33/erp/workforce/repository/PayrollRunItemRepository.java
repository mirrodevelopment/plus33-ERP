/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : PayrollRunItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollRunItemController
 * Related Service   : PayrollRunItemService, PayrollRunItemServiceImpl
 * Related Repository: PayrollRunItemRepository
 * Related Entity    : PayrollRunItem
 * Related DTO       : N/A
 * Related Mapper    : PayrollRunItemMapper
 * Related DB Table  : payroll_run_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollRunItemService, PayrollRunItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'payroll_run_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollRunItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollRunItemRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'payroll_run_items' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code payroll_run_items}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PayrollRunItemRepository extends JpaRepository<PayrollRunItem, Long> {
    List<PayrollRunItem> findByPayrollRunId(Long payrollRunId);
    List<PayrollRunItem> findByEmployeeId(Long employeeId);
}