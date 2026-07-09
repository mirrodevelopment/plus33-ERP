/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformAgentMessageRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAgentMessageController
 * Related Service   : PlatformAgentMessageService, PlatformAgentMessageServiceImpl
 * Related Repository: PlatformAgentMessageRepository
 * Related Entity    : PlatformAgentMessage
 * Related DTO       : N/A
 * Related Mapper    : PlatformAgentMessageMapper
 * Related DB Table  : platform_agent_messages
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAgentMessageService, PlatformAgentMessageServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_agent_messages' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformAgentMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAgentMessageRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_agent_messages' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_agent_messages}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PlatformAgentMessageRepository extends JpaRepository<PlatformAgentMessage, Long> {
}