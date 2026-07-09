/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformAgentToolExecutionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAgentToolExecutionController
 * Related Service   : PlatformAgentToolExecutionService, PlatformAgentToolExecutionServiceImpl
 * Related Repository: PlatformAgentToolExecutionRepository
 * Related Entity    : PlatformAgentToolExecution
 * Related DTO       : N/A
 * Related Mapper    : PlatformAgentToolExecutionMapper
 * Related DB Table  : platform_agent_tool_executions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAgentToolExecutionService, PlatformAgentToolExecutionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_agent_tool_executions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformAgentToolExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAgentToolExecutionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_agent_tool_executions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_agent_tool_executions}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformAgentToolExecutionRepository extends JpaRepository<PlatformAgentToolExecution, Long> {
}