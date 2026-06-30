package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.CapacityPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface CapacityPlanRepository extends JpaRepository<CapacityPlan, Long> {
    List<CapacityPlan> findByMrpRunId(Long mrpRunId);
    List<CapacityPlan> findByWorkCenterIdAndPlanningDate(Long workCenterId, LocalDate planningDate);
    List<CapacityPlan> findByWorkCenterIdAndPlanningDateBetween(Long workCenterId, LocalDate start, LocalDate end);
}
