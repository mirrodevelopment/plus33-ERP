/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformDeploymentGroupRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDeploymentGroupController
 * Related Service   : PlatformDeploymentGroupService, PlatformDeploymentGroupServiceImpl
 * Related Repository: PlatformDeploymentGroupRepository
 * Related Entity    : PlatformDeploymentGroup
 * Related DTO       : N/A
 * Related Mapper    : PlatformDeploymentGroupMapper
 * Related DB Table  : platform_deployment_groups
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDeploymentGroupService, PlatformDeploymentGroupServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_deployment_groups' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformDeploymentGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDeploymentGroupRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_deployment_groups' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_deployment_groups}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PlatformDeploymentGroupRepository extends JpaRepository<PlatformDeploymentGroup, Long> {
    Optional<PlatformDeploymentGroup> findByGroupName(String groupName);
}