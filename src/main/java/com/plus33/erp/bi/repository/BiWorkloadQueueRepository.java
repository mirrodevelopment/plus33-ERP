/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiWorkloadQueueRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiWorkloadQueueController
 * Related Service   : BiWorkloadQueueService, BiWorkloadQueueServiceImpl
 * Related Repository: BiWorkloadQueueRepository
 * Related Entity    : BiWorkloadQueue
 * Related DTO       : N/A
 * Related Mapper    : BiWorkloadQueueMapper
 * Related DB Table  : bi_workload_queues
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiWorkloadQueueService, BiWorkloadQueueServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_workload_queues' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiWorkloadQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiWorkloadQueueRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bi_workload_queues' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_workload_queues}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BiWorkloadQueueRepository extends JpaRepository<BiWorkloadQueue, Long> {
}