package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.CycleCountTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CycleCountTaskRepository extends JpaRepository<CycleCountTask, Long> {
    List<CycleCountTask> findByPlan_Id(Long planId);
    List<CycleCountTask> findByPlan_IdAndStatus(Long planId, String status);
    List<CycleCountTask> findByAssignedToAndStatus(Long userId, String status);
    List<CycleCountTask> findByLocation_IdAndStatus(Long locationId, String status);
}
