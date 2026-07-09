/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformDispatchPolicyRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformDispatchPolicyController
 * Related Service   : PlatformDispatchPolicyService, PlatformDispatchPolicyServiceImpl
 * Related Repository: PlatformDispatchPolicyRepository
 * Related Entity    : PlatformDispatchPolicy
 * Related DTO       : N/A
 * Related Mapper    : PlatformDispatchPolicyMapper
 * Related DB Table  : platform_dispatch_policys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformDispatchPolicyService, PlatformDispatchPolicyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_dispatch_policys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformDispatchPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformDispatchPolicyRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_dispatch_policys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_dispatch_policys}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformDispatchPolicyRepository extends JpaRepository<PlatformDispatchPolicy, Long> {
}