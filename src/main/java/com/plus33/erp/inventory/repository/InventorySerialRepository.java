/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : InventorySerialRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventorySerialController
 * Related Service   : InventorySerialService, InventorySerialServiceImpl
 * Related Repository: InventorySerialRepository
 * Related Entity    : InventorySerial
 * Related DTO       : N/A
 * Related Mapper    : InventorySerialMapper
 * Related DB Table  : inventory_serials
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventorySerialService, InventorySerialServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'inventory_serials' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventorySerial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventorySerialRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'inventory_serials' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_serials}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface InventorySerialRepository extends JpaRepository<InventorySerial, Long>, JpaSpecificationExecutor<InventorySerial> {

    Optional<InventorySerial> findByCompanyIdAndProductIdAndSerialNumber(Long companyId, Long productId, String serialNumber);

    Optional<InventorySerial> findBySerialNumber(String serialNumber);

    List<InventorySerial> findByLotId(Long lotId);
}
