/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.repository
 * File              : IntegrationSagaRepository.java
 * Purpose           : JPA Repository providing database CRUD for Integration Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationSagaController
 * Related Service   : IntegrationSagaService, IntegrationSagaServiceImpl
 * Related Repository: IntegrationSagaRepository
 * Related Entity    : IntegrationSaga
 * Related DTO       : N/A
 * Related Mapper    : IntegrationSagaMapper
 * Related DB Table  : integration_sagas
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationSagaService, IntegrationSagaServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Integration Module against the 'integration_sagas' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.integration.repository;

import com.plus33.erp.integration.entity.IntegrationSaga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationSagaRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'integration_sagas' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_sagas}</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface IntegrationSagaRepository extends JpaRepository<IntegrationSaga, Long> {
}