/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformRouteSimulationRunRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformRouteSimulationRunController
 * Related Service   : PlatformRouteSimulationRunService, PlatformRouteSimulationRunServiceImpl
 * Related Repository: PlatformRouteSimulationRunRepository
 * Related Entity    : PlatformRouteSimulationRun
 * Related DTO       : N/A
 * Related Mapper    : PlatformRouteSimulationRunMapper
 * Related DB Table  : platform_route_simulation_runs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformRouteSimulationRunService, PlatformRouteSimulationRunServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_route_simulation_runs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformRouteSimulationRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformRouteSimulationRunRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_route_simulation_runs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_route_simulation_runs}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformRouteSimulationRunRepository extends JpaRepository<PlatformRouteSimulationRun, Long> {
}