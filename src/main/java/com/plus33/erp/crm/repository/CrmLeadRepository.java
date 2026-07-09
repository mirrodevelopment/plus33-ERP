/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.repository
 * File              : CrmLeadRepository.java
 * Purpose           : JPA Repository providing database CRUD for Crm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmLeadController
 * Related Service   : CrmLeadService, CrmLeadServiceImpl
 * Related Repository: CrmLeadRepository
 * Related Entity    : CrmLead
 * Related DTO       : N/A
 * Related Mapper    : CrmLeadMapper
 * Related DB Table  : crm_leads
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmLeadService, CrmLeadServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Crm Module against the 'crm_leads' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmLead;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmLeadRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'crm_leads' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code crm_leads}</p>
 * <p><b>Module Deps      :</b> Crm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CrmLeadRepository extends JpaRepository<CrmLead, Long> {
    List<CrmLead> findByCompanyId(Long companyId);
}
