/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : EmployeeRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeController
 * Related Service   : EmployeeService, EmployeeServiceImpl
 * Related Repository: EmployeeRepository
 * Related Entity    : Employee
 * Related DTO       : N/A
 * Related Mapper    : EmployeeMapper
 * Related DB Table  : employees
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EmployeeService, EmployeeServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'employees' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'employees' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code employees}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    java.util.List<Employee> findByCompanyId(Long companyId);

    Optional<Employee> findByUserId(Long userId);

    boolean existsByCompanyIdAndEmployeeCode(Long companyId, String employeeCode);

    boolean existsByCompanyIdAndEmail(Long companyId, String email);

    boolean existsByUserId(Long userId);

    boolean existsByUserIdAndIdNot(Long userId, Long employeeId);
}
