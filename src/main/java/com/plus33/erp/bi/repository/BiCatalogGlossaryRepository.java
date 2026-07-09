/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiCatalogGlossaryRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiCatalogGlossaryController
 * Related Service   : BiCatalogGlossaryService, BiCatalogGlossaryServiceImpl
 * Related Repository: BiCatalogGlossaryRepository
 * Related Entity    : BiCatalogGlossary
 * Related DTO       : N/A
 * Related Mapper    : BiCatalogGlossaryMapper
 * Related DB Table  : bi_catalog_glossarys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiCatalogGlossaryService, BiCatalogGlossaryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_catalog_glossarys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiCatalogGlossary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiCatalogGlossaryRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bi_catalog_glossarys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_catalog_glossarys}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BiCatalogGlossaryRepository extends JpaRepository<BiCatalogGlossary, Long> {
}