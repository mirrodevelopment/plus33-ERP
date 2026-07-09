/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : PayrollPolicyVersionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollPolicyVersionController
 * Related Service   : PayrollPolicyVersionService, PayrollPolicyVersionServiceImpl
 * Related Repository: PayrollPolicyVersionRepository
 * Related Entity    : PayrollPolicyVersion
 * Related DTO       : N/A
 * Related Mapper    : PayrollPolicyVersionMapper
 * Related DB Table  : payroll_policy_versions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollPolicyVersionService, PayrollPolicyVersionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'payroll_policy_versions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollPolicyVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollPolicyVersionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'payroll_policy_versions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code payroll_policy_versions}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PayrollPolicyVersionRepository extends JpaRepository<PayrollPolicyVersion, Long> {
    Optional<PayrollPolicyVersion> findByPolicyIdAndVersionNumber(Long policyId, Integer versionNumber);
    List<PayrollPolicyVersion> findByPolicyIdOrderByVersionNumberDesc(Long policyId);
}