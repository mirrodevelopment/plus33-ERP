/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : StockMovementRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockMovementController
 * Related Service   : StockMovementService, StockMovementServiceImpl
 * Related Repository: StockMovementRepository
 * Related Entity    : StockMovement
 * Related DTO       : N/A
 * Related Mapper    : StockMovementMapper
 * Related DB Table  : stock_movements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : StockMovementService, StockMovementServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'stock_movements' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code StockMovementRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'stock_movements' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code stock_movements}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    List<StockMovement> findByProductId(Long productId);

    List<StockMovement> findByWarehouseId(Long warehouseId);

    List<StockMovement> findByStoreId(Long storeId);
}
