package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventoryTransferItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryTransferItemRepository extends JpaRepository<InventoryTransferItem, Long> {
}
