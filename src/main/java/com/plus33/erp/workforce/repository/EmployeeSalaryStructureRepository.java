/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : EmployeeSalaryStructureRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeSalaryStructureController
 * Related Service   : EmployeeSalaryStructureService, EmployeeSalaryStructureServiceImpl
 * Related Repository: EmployeeSalaryStructureRepository
 * Related Entity    : EmployeeSalaryStructure
 * Related DTO       : N/A
 * Related Mapper    : EmployeeSalaryStructureMapper
 * Related DB Table  : employee_salary_structures
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EmployeeSalaryStructureService, EmployeeSalaryStructureServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'employee_salary_structures' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.EmployeeSalaryStructure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeSalaryStructureRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'employee_salary_structures' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code employee_salary_structures}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface EmployeeSalaryStructureRepository extends JpaRepository<EmployeeSalaryStructure, Long> {
    List<EmployeeSalaryStructure> findByEmployeeIdOrderByVersionDesc(Long employeeId);
    Optional<EmployeeSalaryStructure> findByCompanyIdAndEmployeeIdAndStatus(Long companyId, Long employeeId, String status);
}