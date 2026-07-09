/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.repository
 * File              : CrmOpportunityRepository.java
 * Purpose           : JPA Repository providing database CRUD for Crm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmOpportunityController
 * Related Service   : CrmOpportunityService, CrmOpportunityServiceImpl
 * Related Repository: CrmOpportunityRepository
 * Related Entity    : CrmOpportunity
 * Related DTO       : N/A
 * Related Mapper    : CrmOpportunityMapper
 * Related DB Table  : crm_opportunitys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmOpportunityService, CrmOpportunityServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Crm Module against the 'crm_opportunitys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmOpportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmOpportunityRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'crm_opportunitys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code crm_opportunitys}</p>
 * <p><b>Module Deps      :</b> Crm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CrmOpportunityRepository extends JpaRepository<CrmOpportunity, Long> {
    List<CrmOpportunity> findByCompanyId(Long companyId);
    List<CrmOpportunity> findByCompanyIdAndCustomerId(Long companyId, Long customerId);
}
