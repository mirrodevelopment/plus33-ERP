/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformComplianceFrameworkRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformComplianceFrameworkController
 * Related Service   : PlatformComplianceFrameworkService, PlatformComplianceFrameworkServiceImpl
 * Related Repository: PlatformComplianceFrameworkRepository
 * Related Entity    : PlatformComplianceFramework
 * Related DTO       : N/A
 * Related Mapper    : PlatformComplianceFrameworkMapper
 * Related DB Table  : platform_compliance_frameworks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformComplianceFrameworkService, PlatformComplianceFrameworkServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_compliance_frameworks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformComplianceFramework;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformComplianceFrameworkRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_compliance_frameworks' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_compliance_frameworks}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformComplianceFrameworkRepository extends JpaRepository<PlatformComplianceFramework, Long> {
}