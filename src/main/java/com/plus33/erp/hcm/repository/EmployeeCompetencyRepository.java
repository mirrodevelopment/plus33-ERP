/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : EmployeeCompetencyRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeCompetencyController
 * Related Service   : EmployeeCompetencyService, EmployeeCompetencyServiceImpl
 * Related Repository: EmployeeCompetencyRepository
 * Related Entity    : EmployeeCompetency
 * Related DTO       : N/A
 * Related Mapper    : EmployeeCompetencyMapper
 * Related DB Table  : employee_competencys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EmployeeCompetencyService, EmployeeCompetencyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'employee_competencys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.EmployeeCompetency;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeCompetencyRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'employee_competencys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code employee_competencys}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface EmployeeCompetencyRepository extends JpaRepository<EmployeeCompetency, Long> {
    List<EmployeeCompetency> findByEmployeeId(Long employeeId);
}
