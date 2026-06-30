package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ProductionRework;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductionReworkRepository extends JpaRepository<ProductionRework, Long> {
    List<ProductionRework> findByOriginalProductionOrderId(Long originalProductionOrderId);
    List<ProductionRework> findByReworkProductionOrderId(Long reworkProductionOrderId);
}
