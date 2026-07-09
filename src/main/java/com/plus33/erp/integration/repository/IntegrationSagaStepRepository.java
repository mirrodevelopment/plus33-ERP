/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.repository
 * File              : IntegrationSagaStepRepository.java
 * Purpose           : JPA Repository providing database CRUD for Integration Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationSagaStepController
 * Related Service   : IntegrationSagaStepService, IntegrationSagaStepServiceImpl
 * Related Repository: IntegrationSagaStepRepository
 * Related Entity    : IntegrationSagaStep
 * Related DTO       : N/A
 * Related Mapper    : IntegrationSagaStepMapper
 * Related DB Table  : integration_saga_steps
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationSagaStepService, IntegrationSagaStepServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Integration Module against the 'integration_saga_steps' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.integration.repository;

import com.plus33.erp.integration.entity.IntegrationSagaStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationSagaStepRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'integration_saga_steps' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_saga_steps}</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface IntegrationSagaStepRepository extends JpaRepository<IntegrationSagaStep, Long> {
}