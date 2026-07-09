/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformK8sResourceRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformK8sResourceController
 * Related Service   : PlatformK8sResourceService, PlatformK8sResourceServiceImpl
 * Related Repository: PlatformK8sResourceRepository
 * Related Entity    : PlatformK8sResource
 * Related DTO       : N/A
 * Related Mapper    : PlatformK8sResourceMapper
 * Related DB Table  : platform_k8s_resources
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformK8sResourceService, PlatformK8sResourceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_k8s_resources' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformK8sResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformK8sResourceRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_k8s_resources' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_k8s_resources}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformK8sResourceRepository extends JpaRepository<PlatformK8sResource, Long> {
}