/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.repository
 * File              : ProjectBillingContractRepository.java
 * Purpose           : JPA Repository providing database CRUD for Project Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectBillingContractController
 * Related Service   : ProjectBillingContractService, ProjectBillingContractServiceImpl
 * Related Repository: ProjectBillingContractRepository
 * Related Entity    : ProjectBillingContract
 * Related DTO       : N/A
 * Related Mapper    : ProjectBillingContractMapper
 * Related DB Table  : project_billing_contracts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectBillingContractService, ProjectBillingContractServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Project Module against the 'project_billing_contracts' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectBillingContract;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectBillingContractRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'project_billing_contracts' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code project_billing_contracts}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProjectBillingContractRepository extends JpaRepository<ProjectBillingContract, Long> {
    Optional<ProjectBillingContract> findByProjectId(Long projectId);
}
