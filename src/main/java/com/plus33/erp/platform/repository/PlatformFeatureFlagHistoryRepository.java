/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformFeatureFlagHistoryRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformFeatureFlagHistoryController
 * Related Service   : PlatformFeatureFlagHistoryService, PlatformFeatureFlagHistoryServiceImpl
 * Related Repository: PlatformFeatureFlagHistoryRepository
 * Related Entity    : PlatformFeatureFlagHistory
 * Related DTO       : N/A
 * Related Mapper    : PlatformFeatureFlagHistoryMapper
 * Related DB Table  : platform_feature_flag_historys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformFeatureFlagHistoryService, PlatformFeatureFlagHistoryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_feature_flag_historys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformFeatureFlagHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformFeatureFlagHistoryRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_feature_flag_historys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_feature_flag_historys}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PlatformFeatureFlagHistoryRepository extends JpaRepository<PlatformFeatureFlagHistory, Long> {
    List<PlatformFeatureFlagHistory> findByFlagKeyOrderByChangedAtDesc(String flagKey);
}