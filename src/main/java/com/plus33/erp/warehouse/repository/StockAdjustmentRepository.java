package com.plus33.erp.warehouse.repository;

import com.plus33.erp.warehouse.entity.StockAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockAdjustmentRepository extends JpaRepository<StockAdjustment, Long> {
    Optional<StockAdjustment> findByAdjustmentNumber(String adjustmentNumber);
}
