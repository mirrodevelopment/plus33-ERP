/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiKpiDefinitionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiKpiDefinitionController
 * Related Service   : BiKpiDefinitionService, BiKpiDefinitionServiceImpl
 * Related Repository: BiKpiDefinitionRepository
 * Related Entity    : BiKpiDefinition
 * Related DTO       : N/A
 * Related Mapper    : BiKpiDefinitionMapper
 * Related DB Table  : bi_kpi_definitions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiKpiDefinitionService, BiKpiDefinitionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_kpi_definitions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiKpiDefinition;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiKpiDefinitionRepository extends JpaRepository<BiKpiDefinition, Long> {
    java.util.List<BiKpiDefinition> findByStatus(String status);
    java.util.Optional<BiKpiDefinition> findByKpiCode(String kpiCode);
}
