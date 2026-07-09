/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : InventoryLotRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryLotController
 * Related Service   : InventoryLotService, InventoryLotServiceImpl
 * Related Repository: InventoryLotRepository
 * Related Entity    : InventoryLot
 * Related DTO       : N/A
 * Related Mapper    : InventoryLotMapper
 * Related DB Table  : inventory_lots
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryLotService, InventoryLotServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'inventory_lots' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventoryLot;
import com.plus33.erp.inventory.entity.InventoryLotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryLotRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'inventory_lots' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_lots}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface InventoryLotRepository extends JpaRepository<InventoryLot, Long>, JpaSpecificationExecutor<InventoryLot> {

    Optional<InventoryLot> findByCompanyIdAndProductIdAndLotNumber(Long companyId, Long productId, String lotNumber);

    List<InventoryLot> findByProductIdAndStatusOrderByExpiryDateAsc(Long productId, InventoryLotStatus status);

    @Query("SELECT il FROM InventoryLot il WHERE il.status = :status AND il.expiryDate < :date")
    List<InventoryLot> findActiveExpiredLots(@Param("status") InventoryLotStatus status, @Param("date") LocalDate date);
}
