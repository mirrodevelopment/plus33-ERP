/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : InventoryStockRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryStockController
 * Related Service   : InventoryStockService, InventoryStockServiceImpl
 * Related Repository: InventoryStockRepository
 * Related Entity    : InventoryStock
 * Related DTO       : N/A
 * Related Mapper    : InventoryStockMapper
 * Related DB Table  : inventory_stocks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryStockService, InventoryStockServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'inventory_stocks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventoryStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryStockRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'inventory_stocks' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_stocks}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface InventoryStockRepository extends JpaRepository<InventoryStock, Long> {

    List<InventoryStock> findByProductId(Long productId);

    List<InventoryStock> findByWarehouseId(Long warehouseId);

    List<InventoryStock> findByStoreId(Long storeId);

    Optional<InventoryStock> findByProductIdAndWarehouseId(Long productId, Long warehouseId);

    Optional<InventoryStock> findByProductIdAndStoreId(Long productId, Long storeId);

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("SELECT i FROM InventoryStock i WHERE i.product.id = :productId AND i.warehouse.id = :warehouseId")
    Optional<InventoryStock> findWithPessimisticWriteByProductIdAndWarehouseId(Long productId, Long warehouseId);

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("SELECT i FROM InventoryStock i WHERE i.product.id = :productId AND i.store.id = :storeId")
    Optional<InventoryStock> findWithPessimisticWriteByProductIdAndStoreId(Long productId, Long storeId);
}
