/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.repository
 * File              : InstalledAssetRepository.java
 * Purpose           : JPA Repository providing database CRUD for Esm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InstalledAssetController
 * Related Service   : InstalledAssetService, InstalledAssetServiceImpl
 * Related Repository: InstalledAssetRepository
 * Related Entity    : InstalledAsset
 * Related DTO       : N/A
 * Related Mapper    : InstalledAssetMapper
 * Related DB Table  : installed_assets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InstalledAssetService, InstalledAssetServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Esm Module against the 'installed_assets' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.InstalledAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code InstalledAssetRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'installed_assets' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code installed_assets}</p>
 * <p><b>Module Deps      :</b> Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface InstalledAssetRepository extends JpaRepository<InstalledAsset, Long> {
    Optional<InstalledAsset> findBySerialNumber(String serialNumber);
}
