/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.repository
 * File              : ProcessedMessageRepository.java
 * Purpose           : JPA Repository providing database CRUD for Integration Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcessedMessageController
 * Related Service   : ProcessedMessageService, ProcessedMessageServiceImpl
 * Related Repository: ProcessedMessageRepository
 * Related Entity    : ProcessedMessage
 * Related DTO       : N/A
 * Related Mapper    : ProcessedMessageMapper
 * Related DB Table  : processed_messages
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProcessedMessageService, ProcessedMessageServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Integration Module against the 'processed_messages' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.integration.repository;

import com.plus33.erp.integration.entity.ProcessedMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code ProcessedMessageRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'processed_messages' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code processed_messages}</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface ProcessedMessageRepository extends JpaRepository<ProcessedMessage, Long> {
}