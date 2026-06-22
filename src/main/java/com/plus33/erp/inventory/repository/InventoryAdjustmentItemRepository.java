package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventoryAdjustmentItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryAdjustmentItemRepository extends JpaRepository<InventoryAdjustmentItem, Long> {
}
