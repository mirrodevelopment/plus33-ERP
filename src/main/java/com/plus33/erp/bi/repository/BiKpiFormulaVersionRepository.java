/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiKpiFormulaVersionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiKpiFormulaVersionController
 * Related Service   : BiKpiFormulaVersionService, BiKpiFormulaVersionServiceImpl
 * Related Repository: BiKpiFormulaVersionRepository
 * Related Entity    : BiKpiFormulaVersion
 * Related DTO       : N/A
 * Related Mapper    : BiKpiFormulaVersionMapper
 * Related DB Table  : bi_kpi_formula_versions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiKpiFormulaVersionService, BiKpiFormulaVersionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_kpi_formula_versions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiKpiFormulaVersion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiKpiFormulaVersionRepository extends JpaRepository<BiKpiFormulaVersion, Long> {
    java.util.Optional<BiKpiFormulaVersion> findByKpiIdAndIsCurrentTrue(Long kpiId);
    java.util.List<BiKpiFormulaVersion> findByKpiIdOrderByVersionNumberDesc(Long kpiId);
}
