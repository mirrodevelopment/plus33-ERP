/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.repository
 * File              : ProcurementPolicyRepository.java
 * Purpose           : JPA Repository providing database CRUD for Procurement Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcurementPolicyController
 * Related Service   : ProcurementPolicyService, ProcurementPolicyServiceImpl
 * Related Repository: ProcurementPolicyRepository
 * Related Entity    : ProcurementPolicy
 * Related DTO       : N/A
 * Related Mapper    : ProcurementPolicyMapper
 * Related DB Table  : procurement_policys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProcurementPolicyService, ProcurementPolicyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Procurement Module against the 'procurement_policys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.ProcurementPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code ProcurementPolicyRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'procurement_policys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code procurement_policys}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProcurementPolicyRepository extends JpaRepository<ProcurementPolicy, Long> {
    List<ProcurementPolicy> findByCompanyIdAndPolicyTypeAndActive(Long companyId, String policyType, Boolean active);
}
