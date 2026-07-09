/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.repository
 * File              : PlatformSecretDefinitionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Platform Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformSecretDefinitionController
 * Related Service   : PlatformSecretDefinitionService, PlatformSecretDefinitionServiceImpl
 * Related Repository: PlatformSecretDefinitionRepository
 * Related Entity    : PlatformSecretDefinition
 * Related DTO       : N/A
 * Related Mapper    : PlatformSecretDefinitionMapper
 * Related DB Table  : platform_secret_definitions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformSecretDefinitionService, PlatformSecretDefinitionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Platform Module against the 'platform_secret_definitions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.platform.repository;

import com.plus33.erp.platform.entity.PlatformSecretDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformSecretDefinitionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'platform_secret_definitions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_secret_definitions}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PlatformSecretDefinitionRepository extends JpaRepository<PlatformSecretDefinition, Long> {
    Optional<PlatformSecretDefinition> findByAliasPath(String aliasPath);
}