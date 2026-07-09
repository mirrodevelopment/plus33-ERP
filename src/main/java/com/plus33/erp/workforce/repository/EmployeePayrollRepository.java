/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : EmployeePayrollRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeePayrollController
 * Related Service   : EmployeePayrollService, EmployeePayrollServiceImpl
 * Related Repository: EmployeePayrollRepository
 * Related Entity    : EmployeePayroll
 * Related DTO       : N/A
 * Related Mapper    : EmployeePayrollMapper
 * Related DB Table  : employee_payrolls
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EmployeePayrollService, EmployeePayrollServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'employee_payrolls' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.EmployeePayroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeePayrollRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'employee_payrolls' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code employee_payrolls}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface EmployeePayrollRepository extends JpaRepository<EmployeePayroll, Long> {

    Optional<EmployeePayroll> findByPayrollPeriodIdAndEmployeeId(Long payrollPeriodId, Long employeeId);
}
