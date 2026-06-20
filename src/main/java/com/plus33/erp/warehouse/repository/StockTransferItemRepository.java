package com.plus33.erp.warehouse.repository;

import com.plus33.erp.warehouse.entity.StockTransferItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockTransferItemRepository extends JpaRepository<StockTransferItem, Long> {
    List<StockTransferItem> findByStockTransferId(Long stockTransferId);
}
