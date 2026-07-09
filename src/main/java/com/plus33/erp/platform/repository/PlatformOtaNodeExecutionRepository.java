/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformOtaNodeExecutionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformOtaNodeExecutionController
 * Related Service   : PlatformOtaNodeExecutionService, PlatformOtaNodeExecutionServiceImpl
 * Related Repository: PlatformOtaNodeExecutionRepository
 * Related Entity    : PlatformOtaNodeExecution
 * Related DTO       : N/A
 * Related Mapper    : PlatformOtaNodeExecutionMapper
 * Related DB Table  : platform_ota_node_executions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformOtaNodeExecutionService, PlatformOtaNodeExecutionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_ota_node_executions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformOtaNodeExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformOtaNodeExecutionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_ota_node_executions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_ota_node_executions}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformOtaNodeExecutionRepository extends JpaRepository<PlatformOtaNodeExecution, Long> {
}