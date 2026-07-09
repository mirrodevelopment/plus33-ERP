/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : EquipmentAssetRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EquipmentAssetController
 * Related Service   : EquipmentAssetService, EquipmentAssetServiceImpl
 * Related Repository: EquipmentAssetRepository
 * Related Entity    : EquipmentAsset
 * Related DTO       : N/A
 * Related Mapper    : EquipmentAssetMapper
 * Related DB Table  : equipment_assets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EquipmentAssetService, EquipmentAssetServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'equipment_assets' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.EquipmentAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code EquipmentAssetRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'equipment_assets' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code equipment_assets}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface EquipmentAssetRepository extends JpaRepository<EquipmentAsset, Long> {
    Optional<EquipmentAsset> findByAssetCode(String assetCode);
    List<EquipmentAsset> findByCompanyIdAndWarehouseIdAndStatus(Long companyId, Long warehouseId, String status);
}
