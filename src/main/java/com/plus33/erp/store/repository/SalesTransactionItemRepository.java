package com.plus33.erp.store.repository;

import com.plus33.erp.store.entity.SalesTransactionItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SalesTransactionItemRepository extends JpaRepository<SalesTransactionItem, Long> {
    List<SalesTransactionItem> findBySalesTransactionId(Long salesTransactionId);
}
