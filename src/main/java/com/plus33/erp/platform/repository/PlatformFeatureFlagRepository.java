/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformFeatureFlagRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformFeatureFlagController
 * Related Service   : PlatformFeatureFlagService, PlatformFeatureFlagServiceImpl
 * Related Repository: PlatformFeatureFlagRepository
 * Related Entity    : PlatformFeatureFlag
 * Related DTO       : N/A
 * Related Mapper    : PlatformFeatureFlagMapper
 * Related DB Table  : platform_feature_flags
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformFeatureFlagService, PlatformFeatureFlagServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_feature_flags' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformFeatureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformFeatureFlagRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_feature_flags' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_feature_flags}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PlatformFeatureFlagRepository extends JpaRepository<PlatformFeatureFlag, Long> {
    Optional<PlatformFeatureFlag> findByFlagKey(String flagKey);
}