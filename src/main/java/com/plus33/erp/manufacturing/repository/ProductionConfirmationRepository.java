package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ProductionConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductionConfirmationRepository extends JpaRepository<ProductionConfirmation, Long> {
    List<ProductionConfirmation> findByProductionOrderId(Long productionOrderId);
    List<ProductionConfirmation> findByProductionOrderOperationId(Long productionOrderOperationId);
}
