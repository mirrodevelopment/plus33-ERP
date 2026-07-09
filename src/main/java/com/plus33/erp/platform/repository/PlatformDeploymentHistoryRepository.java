/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformDeploymentHistoryRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDeploymentHistoryController
 * Related Service   : PlatformDeploymentHistoryService, PlatformDeploymentHistoryServiceImpl
 * Related Repository: PlatformDeploymentHistoryRepository
 * Related Entity    : PlatformDeploymentHistory
 * Related DTO       : N/A
 * Related Mapper    : PlatformDeploymentHistoryMapper
 * Related DB Table  : platform_deployment_historys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDeploymentHistoryService, PlatformDeploymentHistoryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_deployment_historys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformDeploymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDeploymentHistoryRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_deployment_historys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_deployment_historys}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PlatformDeploymentHistoryRepository extends JpaRepository<PlatformDeploymentHistory, Long> {
    List<PlatformDeploymentHistory> findByDeploymentVersion(String deploymentVersion);
}