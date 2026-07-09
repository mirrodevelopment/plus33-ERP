/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformConfigVersionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformConfigVersionController
 * Related Service   : PlatformConfigVersionService, PlatformConfigVersionServiceImpl
 * Related Repository: PlatformConfigVersionRepository
 * Related Entity    : PlatformConfigVersion
 * Related DTO       : N/A
 * Related Mapper    : PlatformConfigVersionMapper
 * Related DB Table  : platform_config_versions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformConfigVersionService, PlatformConfigVersionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_config_versions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformConfigVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformConfigVersionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_config_versions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_config_versions}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PlatformConfigVersionRepository extends JpaRepository<PlatformConfigVersion, Long> {
    List<PlatformConfigVersion> findByConfigIdOrderByVersionDesc(Long configId);
}