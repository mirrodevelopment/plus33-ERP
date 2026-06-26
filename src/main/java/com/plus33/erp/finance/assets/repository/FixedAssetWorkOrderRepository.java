package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetWorkOrder;
import com.plus33.erp.finance.assets.entity.WorkOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FixedAssetWorkOrderRepository extends JpaRepository<FixedAssetWorkOrder, Long> {
    List<FixedAssetWorkOrder> findAllByFixedAssetId(Long fixedAssetId);
    List<FixedAssetWorkOrder> findAllByFixedAssetIdOrderByCreatedAtDesc(Long fixedAssetId);
    List<FixedAssetWorkOrder> findAllByStatus(WorkOrderStatus status);
}
