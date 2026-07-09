/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : JobRequisitionVersionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JobRequisitionVersionController
 * Related Service   : JobRequisitionVersionService, JobRequisitionVersionServiceImpl
 * Related Repository: JobRequisitionVersionRepository
 * Related Entity    : JobRequisitionVersion
 * Related DTO       : N/A
 * Related Mapper    : JobRequisitionVersionMapper
 * Related DB Table  : job_requisition_versions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : JobRequisitionVersionService, JobRequisitionVersionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'job_requisition_versions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.JobRequisitionVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code JobRequisitionVersionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'job_requisition_versions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code job_requisition_versions}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface JobRequisitionVersionRepository extends JpaRepository<JobRequisitionVersion, Long> {
    List<JobRequisitionVersion> findByRequisitionId(Long requisitionId);
}
