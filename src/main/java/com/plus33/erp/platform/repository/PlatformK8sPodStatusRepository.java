/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformK8sPodStatusRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformK8sPodStatusController
 * Related Service   : PlatformK8sPodStatusService, PlatformK8sPodStatusServiceImpl
 * Related Repository: PlatformK8sPodStatusRepository
 * Related Entity    : PlatformK8sPodStatus
 * Related DTO       : N/A
 * Related Mapper    : PlatformK8sPodStatusMapper
 * Related DB Table  : platform_k8s_pod_statuss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformK8sPodStatusService, PlatformK8sPodStatusServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_k8s_pod_statuss' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformK8sPodStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformK8sPodStatusRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_k8s_pod_statuss' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_k8s_pod_statuss}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformK8sPodStatusRepository extends JpaRepository<PlatformK8sPodStatus, Long> {
}