/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiSchemaEvolutionHistoryRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiSchemaEvolutionHistoryController
 * Related Service   : BiSchemaEvolutionHistoryService, BiSchemaEvolutionHistoryServiceImpl
 * Related Repository: BiSchemaEvolutionHistoryRepository
 * Related Entity    : BiSchemaEvolutionHistory
 * Related DTO       : N/A
 * Related Mapper    : BiSchemaEvolutionHistoryMapper
 * Related DB Table  : bi_schema_evolution_historys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiSchemaEvolutionHistoryService, BiSchemaEvolutionHistoryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_schema_evolution_historys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiSchemaEvolutionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiSchemaEvolutionHistoryRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bi_schema_evolution_historys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_schema_evolution_historys}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BiSchemaEvolutionHistoryRepository extends JpaRepository<BiSchemaEvolutionHistory, Long> {
}