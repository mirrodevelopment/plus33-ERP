/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : WarehouseZoneRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseZoneController
 * Related Service   : WarehouseZoneService, WarehouseZoneServiceImpl
 * Related Repository: WarehouseZoneRepository
 * Related Entity    : WarehouseZone
 * Related DTO       : N/A
 * Related Mapper    : WarehouseZoneMapper
 * Related DB Table  : warehouse_zones
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseZoneService, WarehouseZoneServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'warehouse_zones' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseZone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseZoneRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'warehouse_zones' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_zones}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface WarehouseZoneRepository extends JpaRepository<WarehouseZone, Long> {
    Optional<WarehouseZone> findByWarehouseIdAndCode(Long warehouseId, String code);
    List<WarehouseZone> findByCompanyIdAndWarehouseId(Long companyId, Long warehouseId);
    List<WarehouseZone> findByCompanyIdAndWarehouseIdAndActiveTrue(Long companyId, Long warehouseId);
}
