/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.repository
 * File              : JobRequisitionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Hcm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: JobRequisitionController
 * Related Service   : JobRequisitionService, JobRequisitionServiceImpl
 * Related Repository: JobRequisitionRepository
 * Related Entity    : JobRequisition
 * Related DTO       : N/A
 * Related Mapper    : JobRequisitionMapper
 * Related DB Table  : job_requisitions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : JobRequisitionService, JobRequisitionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Hcm Module against the 'job_requisitions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.JobRequisition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code JobRequisitionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'job_requisitions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code job_requisitions}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface JobRequisitionRepository extends JpaRepository<JobRequisition, Long> {
    Optional<JobRequisition> findByRequisitionNumber(String requisitionNumber);
}
