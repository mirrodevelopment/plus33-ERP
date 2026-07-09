/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.repository
 * File              : CrmCommissionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Crm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmCommissionController
 * Related Service   : CrmCommissionService, CrmCommissionServiceImpl
 * Related Repository: CrmCommissionRepository
 * Related Entity    : CrmCommission
 * Related DTO       : N/A
 * Related Mapper    : CrmCommissionMapper
 * Related DB Table  : crm_commissions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmCommissionService, CrmCommissionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Crm Module against the 'crm_commissions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.crm.repository;

import com.plus33.erp.crm.entity.CrmCommission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmCommissionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'crm_commissions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code crm_commissions}</p>
 * <p><b>Module Deps      :</b> Crm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface CrmCommissionRepository extends JpaRepository<CrmCommission, Long> {
    List<CrmCommission> findByCompanyIdAndSalesRepId(Long companyId, Long salesRepId);
}
