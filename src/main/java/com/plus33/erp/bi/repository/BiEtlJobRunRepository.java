/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiEtlJobRunRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiEtlJobRunController
 * Related Service   : BiEtlJobRunService, BiEtlJobRunServiceImpl
 * Related Repository: BiEtlJobRunRepository
 * Related Entity    : BiEtlJobRun
 * Related DTO       : N/A
 * Related Mapper    : BiEtlJobRunMapper
 * Related DB Table  : bi_etl_job_runs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiEtlJobRunService, BiEtlJobRunServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_etl_job_runs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiEtlJobRun;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiEtlJobRunRepository extends JpaRepository<BiEtlJobRun, Long> {
    java.util.List<BiEtlJobRun> findByJobIdOrderByCreatedAtDesc(Long jobId);
    java.util.Optional<BiEtlJobRun> findTopByJobIdAndStatusOrderByCreatedAtDesc(Long jobId, String status);
}
