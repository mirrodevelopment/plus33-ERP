/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : PayrollPolicyRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollPolicyController
 * Related Service   : PayrollPolicyService, PayrollPolicyServiceImpl
 * Related Repository: PayrollPolicyRepository
 * Related Entity    : PayrollPolicy
 * Related DTO       : N/A
 * Related Mapper    : PayrollPolicyMapper
 * Related DB Table  : payroll_policys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollPolicyService, PayrollPolicyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'payroll_policys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.PayrollPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollPolicyRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'payroll_policys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code payroll_policys}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PayrollPolicyRepository extends JpaRepository<PayrollPolicy, Long> {
    Optional<PayrollPolicy> findByCompanyIdAndCode(Long companyId, String code);
    List<PayrollPolicy> findByCompanyId(Long companyId);
}