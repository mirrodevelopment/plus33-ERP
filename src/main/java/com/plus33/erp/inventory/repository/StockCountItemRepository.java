/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : StockCountItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountItemController
 * Related Service   : StockCountItemService, StockCountItemServiceImpl
 * Related Repository: StockCountItemRepository
 * Related Entity    : StockCountItem
 * Related DTO       : N/A
 * Related Mapper    : StockCountItemMapper
 * Related DB Table  : stock_count_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : StockCountItemService, StockCountItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'stock_count_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.StockCountItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockCountItemRepository extends JpaRepository<StockCountItem, Long> {
}
