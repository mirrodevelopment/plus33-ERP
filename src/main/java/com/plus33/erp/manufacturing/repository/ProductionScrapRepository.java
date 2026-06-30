package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ProductionScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductionScrapRepository extends JpaRepository<ProductionScrap, Long> {
    List<ProductionScrap> findByProductionOrderId(Long productionOrderId);
    List<ProductionScrap> findByProductionOrderOperationId(Long productionOrderOperationId);
}
