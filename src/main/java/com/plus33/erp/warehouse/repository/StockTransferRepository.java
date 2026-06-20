package com.plus33.erp.warehouse.repository;

import com.plus33.erp.warehouse.entity.StockTransfer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StockTransferRepository extends JpaRepository<StockTransfer, Long> {
    Optional<StockTransfer> findByTransferNumber(String transferNumber);
}
