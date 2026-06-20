package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long>, JpaSpecificationExecutor<PurchaseOrder> {
    Optional<PurchaseOrder> findByOrderNumber(String orderNumber);

    @Query(value = "SELECT nextval('purchase_order_seq')", nativeQuery = true)
    Long getNextSequenceValue();

    boolean existsByPurchaseRequestId(Long purchaseRequestId);
    boolean existsByPurchaseRequestIdAndIdNot(Long purchaseRequestId, Long id);
}
