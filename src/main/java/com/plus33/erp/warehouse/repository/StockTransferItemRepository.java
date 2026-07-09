/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Warehouse Module
 * Package           : com.plus33.erp.warehouse.repository
 * File              : StockTransferItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Warehouse Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockTransferItemController
 * Related Service   : StockTransferItemService, StockTransferItemServiceImpl
 * Related Repository: StockTransferItemRepository
 * Related Entity    : StockTransferItem
 * Related DTO       : N/A
 * Related Mapper    : StockTransferItemMapper
 * Related DB Table  : stock_transfer_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : StockTransferItemService, StockTransferItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Warehouse Module against the 'stock_transfer_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.warehouse.repository;

import com.plus33.erp.warehouse.entity.StockTransferItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Warehouse Module</b>
 *
 * <p><b>Class  :</b> {@code StockTransferItemRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.warehouse.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'stock_transfer_items' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code stock_transfer_items}</p>
 * <p><b>Module Deps      :</b> Warehouse</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface StockTransferItemRepository extends JpaRepository<StockTransferItem, Long> {
    List<StockTransferItem> findByStockTransferId(Long stockTransferId);
}
