package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.PurchaseRequestItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseRequestItemRepository extends JpaRepository<PurchaseRequestItem, Long> {
    List<PurchaseRequestItem> findByPurchaseRequestId(Long purchaseRequestId);
}
