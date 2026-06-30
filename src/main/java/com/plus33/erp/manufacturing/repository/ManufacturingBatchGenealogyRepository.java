package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ManufacturingBatchGenealogy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ManufacturingBatchGenealogyRepository extends JpaRepository<ManufacturingBatchGenealogy, Long> {
    List<ManufacturingBatchGenealogy> findByProductionOrderId(Long productionOrderId);
    List<ManufacturingBatchGenealogy> findByBatchNumber(String batchNumber);
    List<ManufacturingBatchGenealogy> findByParentBatchNumber(String parentBatchNumber);
}
