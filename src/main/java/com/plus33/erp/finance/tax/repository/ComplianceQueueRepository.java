/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.repository
 * File              : ComplianceQueueRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ComplianceQueueController
 * Related Service   : ComplianceQueueService, ComplianceQueueServiceImpl
 * Related Repository: ComplianceQueueRepository
 * Related Entity    : ComplianceQueue
 * Related DTO       : N/A
 * Related Mapper    : ComplianceQueueMapper
 * Related DB Table  : compliance_queues
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ComplianceQueueService, ComplianceQueueServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'compliance_queues' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.ComplianceQueueItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code ComplianceQueueRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'compliance_queues' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code compliance_queues}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface ComplianceQueueRepository extends JpaRepository<ComplianceQueueItem, Long> {
    List<ComplianceQueueItem> findByStatusIn(List<String> statuses);
}