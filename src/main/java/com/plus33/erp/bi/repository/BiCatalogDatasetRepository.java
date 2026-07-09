/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiCatalogDatasetRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiCatalogDatasetController
 * Related Service   : BiCatalogDatasetService, BiCatalogDatasetServiceImpl
 * Related Repository: BiCatalogDatasetRepository
 * Related Entity    : BiCatalogDataset
 * Related DTO       : N/A
 * Related Mapper    : BiCatalogDatasetMapper
 * Related DB Table  : bi_catalog_datasets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiCatalogDatasetService, BiCatalogDatasetServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_catalog_datasets' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiCatalogDataset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiCatalogDatasetRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bi_catalog_datasets' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_catalog_datasets}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BiCatalogDatasetRepository extends JpaRepository<BiCatalogDataset, Long> {
}