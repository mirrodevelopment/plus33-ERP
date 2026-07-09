/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.repository
 * File              : IntegrationWorkflowInstanceRepository.java
 * Purpose           : JPA Repository providing database CRUD for Integration Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationWorkflowInstanceController
 * Related Service   : IntegrationWorkflowInstanceService, IntegrationWorkflowInstanceServiceImpl
 * Related Repository: IntegrationWorkflowInstanceRepository
 * Related Entity    : IntegrationWorkflowInstance
 * Related DTO       : N/A
 * Related Mapper    : IntegrationWorkflowInstanceMapper
 * Related DB Table  : integration_workflow_instances
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationWorkflowInstanceService, IntegrationWorkflowInstanceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Integration Module against the 'integration_workflow_instances' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.integration.repository;

import com.plus33.erp.integration.entity.IntegrationWorkflowInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationWorkflowInstanceRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'integration_workflow_instances' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_workflow_instances}</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface IntegrationWorkflowInstanceRepository extends JpaRepository<IntegrationWorkflowInstance, Long> {
}