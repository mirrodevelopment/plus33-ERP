/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.repository
 * File              : BiBrokerRegistryRepository.java
 * Purpose           : JPA Repository providing database CRUD for Integration Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiBrokerRegistryController
 * Related Service   : BiBrokerRegistryService, BiBrokerRegistryServiceImpl
 * Related Repository: BiBrokerRegistryRepository
 * Related Entity    : BiBrokerRegistry
 * Related DTO       : N/A
 * Related Mapper    : BiBrokerRegistryMapper
 * Related DB Table  : bi_broker_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiBrokerRegistryService, BiBrokerRegistryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Integration Module against the 'bi_broker_registrys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.integration.repository;

import com.plus33.erp.integration.entity.BiBrokerRegistry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code BiBrokerRegistryRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bi_broker_registrys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_broker_registrys}</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BiBrokerRegistryRepository extends JpaRepository<BiBrokerRegistry, Long> {
}