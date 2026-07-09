/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : HcmOrganizationRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: HcmOrganizationController
 * Related Service   : HcmOrganizationService, HcmOrganizationServiceImpl
 * Related Repository: HcmOrganizationRepository
 * Related Entity    : HcmOrganization
 * Related DTO       : N/A
 * Related Mapper    : HcmOrganizationMapper
 * Related DB Table  : hcm_organizations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : HcmOrganizationService, HcmOrganizationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'hcm_organizations' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.HcmOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code HcmOrganizationRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'hcm_organizations' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code hcm_organizations}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface HcmOrganizationRepository extends JpaRepository<HcmOrganization, Long> {
    List<HcmOrganization> findByCompanyIdAndIsCurrent(Long companyId, Boolean isCurrent);
}
