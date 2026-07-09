/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformConfigRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformConfigController
 * Related Service   : PlatformConfigService, PlatformConfigServiceImpl
 * Related Repository: PlatformConfigRepository
 * Related Entity    : PlatformConfig
 * Related DTO       : N/A
 * Related Mapper    : PlatformConfigMapper
 * Related DB Table  : platform_configs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformConfigService, PlatformConfigServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_configs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformConfigRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_configs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_configs}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PlatformConfigRepository extends JpaRepository<PlatformConfig, Long> {
    Optional<PlatformConfig> findByConfigKey(String configKey);
}