package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ProductionOrderOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductionOrderOperationRepository extends JpaRepository<ProductionOrderOperation, Long> {
    List<ProductionOrderOperation> findByProductionOrderId(Long productionOrderId);
}
