/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.repository
 * File              : WarehouseRepository.java
 * Purpose           : JPA Repository providing database CRUD for Organization Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseController
 * Related Service   : WarehouseService, WarehouseServiceImpl
 * Related Repository: WarehouseRepository
 * Related Entity    : Warehouse
 * Related DTO       : N/A
 * Related Mapper    : WarehouseMapper
 * Related DB Table  : warehouses
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseService, WarehouseServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Organization Module against the 'warehouses' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.organization.repository;

import com.plus33.erp.organization.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'warehouses' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouses}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {
    Optional<Warehouse> findByCode(String code);
    boolean existsByCode(String code);
    boolean existsByRegionIdAndCode(Long regionId, String code);
    boolean existsByRegionIdAndActiveTrue(Long regionId);
}
