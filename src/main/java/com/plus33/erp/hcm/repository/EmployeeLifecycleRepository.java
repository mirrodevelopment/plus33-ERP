/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : EmployeeLifecycleRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeLifecycleController
 * Related Service   : EmployeeLifecycleService, EmployeeLifecycleServiceImpl
 * Related Repository: EmployeeLifecycleRepository
 * Related Entity    : EmployeeLifecycle
 * Related DTO       : N/A
 * Related Mapper    : EmployeeLifecycleMapper
 * Related DB Table  : employee_lifecycles
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EmployeeLifecycleService, EmployeeLifecycleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'employee_lifecycles' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.EmployeeLifecycle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeLifecycleRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'employee_lifecycles' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code employee_lifecycles}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface EmployeeLifecycleRepository extends JpaRepository<EmployeeLifecycle, Long> {
    Optional<EmployeeLifecycle> findByEmployeeId(Long employeeId);
}
