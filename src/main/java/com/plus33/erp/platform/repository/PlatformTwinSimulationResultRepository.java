/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformTwinSimulationResultRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTwinSimulationResultController
 * Related Service   : PlatformTwinSimulationResultService, PlatformTwinSimulationResultServiceImpl
 * Related Repository: PlatformTwinSimulationResultRepository
 * Related Entity    : PlatformTwinSimulationResult
 * Related DTO       : N/A
 * Related Mapper    : PlatformTwinSimulationResultMapper
 * Related DB Table  : platform_twin_simulation_results
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTwinSimulationResultService, PlatformTwinSimulationResultServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_twin_simulation_results' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformTwinSimulationResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTwinSimulationResultRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_twin_simulation_results' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_twin_simulation_results}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformTwinSimulationResultRepository extends JpaRepository<PlatformTwinSimulationResult, Long> {
}