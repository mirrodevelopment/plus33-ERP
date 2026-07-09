/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformPredictiveMaintenancePolicyRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformPredictiveMaintenancePolicyController
 * Related Service   : PlatformPredictiveMaintenancePolicyService, PlatformPredictiveMaintenancePolicyServiceImpl
 * Related Repository: PlatformPredictiveMaintenancePolicyRepository
 * Related Entity    : PlatformPredictiveMaintenancePolicy
 * Related DTO       : N/A
 * Related Mapper    : PlatformPredictiveMaintenancePolicyMapper
 * Related DB Table  : platform_predictive_maintenance_policys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformPredictiveMaintenancePolicyService, PlatformPredictiveMaintenancePolicyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_predictive_maintenance_policys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformPredictiveMaintenancePolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformPredictiveMaintenancePolicyRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_predictive_maintenance_policys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_predictive_maintenance_policys}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformPredictiveMaintenancePolicyRepository extends JpaRepository<PlatformPredictiveMaintenancePolicy, Long> {
}