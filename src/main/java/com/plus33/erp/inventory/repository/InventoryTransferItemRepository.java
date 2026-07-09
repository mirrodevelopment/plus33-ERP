/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : InventoryTransferItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTransferItemController
 * Related Service   : InventoryTransferItemService, InventoryTransferItemServiceImpl
 * Related Repository: InventoryTransferItemRepository
 * Related Entity    : InventoryTransferItem
 * Related DTO       : N/A
 * Related Mapper    : InventoryTransferItemMapper
 * Related DB Table  : inventory_transfer_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryTransferItemService, InventoryTransferItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'inventory_transfer_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventoryTransferItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryTransferItemRepository extends JpaRepository<InventoryTransferItem, Long> {
}
