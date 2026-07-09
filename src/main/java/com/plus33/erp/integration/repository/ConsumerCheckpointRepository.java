/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.repository
 * File              : ConsumerCheckpointRepository.java
 * Purpose           : JPA Repository providing database CRUD for Integration Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ConsumerCheckpointController
 * Related Service   : ConsumerCheckpointService, ConsumerCheckpointServiceImpl
 * Related Repository: ConsumerCheckpointRepository
 * Related Entity    : ConsumerCheckpoint
 * Related DTO       : N/A
 * Related Mapper    : ConsumerCheckpointMapper
 * Related DB Table  : consumer_checkpoints
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ConsumerCheckpointService, ConsumerCheckpointServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Integration Module against the 'consumer_checkpoints' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.integration.repository;

import com.plus33.erp.integration.entity.ConsumerCheckpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code ConsumerCheckpointRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'consumer_checkpoints' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code consumer_checkpoints}</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface ConsumerCheckpointRepository extends JpaRepository<ConsumerCheckpoint, Long> {
}