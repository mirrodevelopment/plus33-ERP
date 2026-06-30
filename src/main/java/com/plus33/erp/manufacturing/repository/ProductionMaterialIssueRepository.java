package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ProductionMaterialIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductionMaterialIssueRepository extends JpaRepository<ProductionMaterialIssue, Long> {
    List<ProductionMaterialIssue> findByProductionOrderId(Long productionOrderId);
    List<ProductionMaterialIssue> findByProductionOrderIdAndBomLineId(Long productionOrderId, Long bomLineId);
}
