/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiFactLoadAuditRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiFactLoadAuditController
 * Related Service   : BiFactLoadAuditService, BiFactLoadAuditServiceImpl
 * Related Repository: BiFactLoadAuditRepository
 * Related Entity    : BiFactLoadAudit
 * Related DTO       : N/A
 * Related Mapper    : BiFactLoadAuditMapper
 * Related DB Table  : bi_fact_load_audits
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiFactLoadAuditService, BiFactLoadAuditServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_fact_load_audits' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiFactLoadAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiFactLoadAuditRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bi_fact_load_audits' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_fact_load_audits}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface BiFactLoadAuditRepository extends JpaRepository<BiFactLoadAudit, Long> {
    List<BiFactLoadAudit> findByFactTableAndStatus(String factTable, String status);
    List<BiFactLoadAudit> findByBatchId(String batchId);
}
