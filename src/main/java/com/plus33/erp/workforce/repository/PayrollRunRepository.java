/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : PayrollRunRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollRunController
 * Related Service   : PayrollRunService, PayrollRunServiceImpl
 * Related Repository: PayrollRunRepository
 * Related Entity    : PayrollRun
 * Related DTO       : N/A
 * Related Mapper    : PayrollRunMapper
 * Related DB Table  : payroll_runs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollRunService, PayrollRunServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'payroll_runs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollRunRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'payroll_runs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code payroll_runs}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PayrollRunRepository extends JpaRepository<PayrollRun, Long> {
    Optional<PayrollRun> findByCompanyIdAndRunNumber(Long companyId, String runNumber);
    List<PayrollRun> findByCompanyId(Long companyId);
}