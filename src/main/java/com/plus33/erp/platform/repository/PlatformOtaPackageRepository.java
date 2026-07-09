/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformOtaPackageRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformOtaPackageController
 * Related Service   : PlatformOtaPackageService, PlatformOtaPackageServiceImpl
 * Related Repository: PlatformOtaPackageRepository
 * Related Entity    : PlatformOtaPackage
 * Related DTO       : N/A
 * Related Mapper    : PlatformOtaPackageMapper
 * Related DB Table  : platform_ota_packages
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformOtaPackageService, PlatformOtaPackageServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_ota_packages' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformOtaPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformOtaPackageRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_ota_packages' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_ota_packages}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformOtaPackageRepository extends JpaRepository<PlatformOtaPackage, Long> {
}