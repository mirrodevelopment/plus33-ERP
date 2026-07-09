/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : InventoryAdjustmentItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAdjustmentItemController
 * Related Service   : InventoryAdjustmentItemService, InventoryAdjustmentItemServiceImpl
 * Related Repository: InventoryAdjustmentItemRepository
 * Related Entity    : InventoryAdjustmentItem
 * Related DTO       : N/A
 * Related Mapper    : InventoryAdjustmentItemMapper
 * Related DB Table  : inventory_adjustment_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryAdjustmentItemService, InventoryAdjustmentItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'inventory_adjustment_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventoryAdjustmentItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryAdjustmentItemRepository extends JpaRepository<InventoryAdjustmentItem, Long> {
}
