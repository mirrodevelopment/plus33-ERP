/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.repository
 * File              : EventMeshRegistryRepository.java
 * Purpose           : JPA Repository providing database CRUD for Integration Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EventMeshRegistryController
 * Related Service   : EventMeshRegistryService, EventMeshRegistryServiceImpl
 * Related Repository: EventMeshRegistryRepository
 * Related Entity    : EventMeshRegistry
 * Related DTO       : N/A
 * Related Mapper    : EventMeshRegistryMapper
 * Related DB Table  : event_mesh_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EventMeshRegistryService, EventMeshRegistryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Integration Module against the 'event_mesh_registrys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.integration.repository;

import com.plus33.erp.integration.entity.EventMeshRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code EventMeshRegistryRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'event_mesh_registrys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code event_mesh_registrys}</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface EventMeshRegistryRepository extends JpaRepository<EventMeshRegistry, Long> {
}