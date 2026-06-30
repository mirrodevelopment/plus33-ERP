package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.CycleCountPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CycleCountPlanRepository extends JpaRepository<CycleCountPlan, Long> {
    Optional<CycleCountPlan> findByCompanyIdAndPlanNumber(Long companyId, String planNumber);
    List<CycleCountPlan> findByCompanyIdAndWarehouseIdAndStatus(Long companyId, Long warehouseId, String status);
}
