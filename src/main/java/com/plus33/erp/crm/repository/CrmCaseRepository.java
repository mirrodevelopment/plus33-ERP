/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.repository
 * File              : CrmCaseRepository.java
 * Purpose           : JPA Repository providing database CRUD for Crm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmCaseController
 * Related Service   : CrmCaseService, CrmCaseServiceImpl
 * Related Repository: CrmCaseRepository
 * Related Entity    : CrmCase
 * Related DTO       : N/A
 * Related Mapper    : CrmCaseMapper
 * Related DB Table  : crm_cases
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmCaseService, CrmCaseServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Crm Module against the 'crm_cases' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmCase;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmCaseRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'crm_cases' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code crm_cases}</p>
 * <p><b>Module Deps      :</b> Crm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CrmCaseRepository extends JpaRepository<CrmCase, Long> {
    List<CrmCase> findByCompanyId(Long companyId);
    List<CrmCase> findByCompanyIdAndCustomerId(Long companyId, Long customerId);
}
