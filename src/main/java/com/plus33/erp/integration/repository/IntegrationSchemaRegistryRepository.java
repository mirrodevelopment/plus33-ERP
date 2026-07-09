/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.repository
 * File              : IntegrationSchemaRegistryRepository.java
 * Purpose           : JPA Repository providing database CRUD for Integration Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationSchemaRegistryController
 * Related Service   : IntegrationSchemaRegistryService, IntegrationSchemaRegistryServiceImpl
 * Related Repository: IntegrationSchemaRegistryRepository
 * Related Entity    : IntegrationSchemaRegistry
 * Related DTO       : N/A
 * Related Mapper    : IntegrationSchemaRegistryMapper
 * Related DB Table  : integration_schema_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationSchemaRegistryService, IntegrationSchemaRegistryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Integration Module against the 'integration_schema_registrys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.integration.repository;

import com.plus33.erp.integration.entity.IntegrationSchemaRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationSchemaRegistryRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'integration_schema_registrys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_schema_registrys}</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface IntegrationSchemaRegistryRepository extends JpaRepository<IntegrationSchemaRegistry, Long> {
}