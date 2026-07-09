/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiEtlJobRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiEtlJobController
 * Related Service   : BiEtlJobService, BiEtlJobServiceImpl
 * Related Repository: BiEtlJobRepository
 * Related Entity    : BiEtlJob
 * Related DTO       : N/A
 * Related Mapper    : BiEtlJobMapper
 * Related DB Table  : bi_etl_jobs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiEtlJobService, BiEtlJobServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_etl_jobs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiEtlJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiEtlJobRepository extends JpaRepository<BiEtlJob, Long> {
    java.util.List<BiEtlJob> findByStatusAndEnabledTrue(String status);
    java.util.List<BiEtlJob> findBySourceModuleAndEnabledTrue(String sourceModule);
}
