package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.ReplenishmentPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReplenishmentPlanRepository extends JpaRepository<ReplenishmentPlan, Long> {
    List<ReplenishmentPlan> findByCompanyIdAndWarehouseIdAndActiveTrue(Long companyId, Long warehouseId);
    List<ReplenishmentPlan> findByWarehouseIdAndProductIdAndActiveTrue(Long warehouseId, Long productId);
}
