package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.StockCountItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockCountItemRepository extends JpaRepository<StockCountItem, Long> {
}
