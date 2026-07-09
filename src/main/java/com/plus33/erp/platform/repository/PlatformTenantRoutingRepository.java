/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformTenantRoutingRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTenantRoutingController
 * Related Service   : PlatformTenantRoutingService, PlatformTenantRoutingServiceImpl
 * Related Repository: PlatformTenantRoutingRepository
 * Related Entity    : PlatformTenantRouting
 * Related DTO       : N/A
 * Related Mapper    : PlatformTenantRoutingMapper
 * Related DB Table  : platform_tenant_routings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTenantRoutingService, PlatformTenantRoutingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_tenant_routings' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformTenantRouting;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTenantRoutingRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_tenant_routings' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_tenant_routings}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PlatformTenantRoutingRepository extends JpaRepository<PlatformTenantRouting, Long> {
    Optional<PlatformTenantRouting> findByTenantId(String tenantId);
}