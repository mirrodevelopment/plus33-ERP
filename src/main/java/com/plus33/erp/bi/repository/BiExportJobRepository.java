/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiExportJobRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiExportJobController
 * Related Service   : BiExportJobService, BiExportJobServiceImpl
 * Related Repository: BiExportJobRepository
 * Related Entity    : BiExportJob
 * Related DTO       : N/A
 * Related Mapper    : BiExportJobMapper
 * Related DB Table  : bi_export_jobs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiExportJobService, BiExportJobServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_export_jobs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiExportJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiExportJobRepository extends JpaRepository<BiExportJob, Long> {
    java.util.List<BiExportJob> findByCompanyIdAndStatus(Long companyId, String status);
    java.util.Optional<BiExportJob> findByJobReference(String jobReference);
}
