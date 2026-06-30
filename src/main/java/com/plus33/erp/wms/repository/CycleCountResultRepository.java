package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.CycleCountResult;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CycleCountResultRepository extends JpaRepository<CycleCountResult, Long> {
    List<CycleCountResult> findByPlanId(Long planId);
    List<CycleCountResult> findByTask_Id(Long taskId);
    List<CycleCountResult> findByPlanIdAndStatus(Long planId, String status);
}
