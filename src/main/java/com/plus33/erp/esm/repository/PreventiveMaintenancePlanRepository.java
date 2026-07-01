package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.PreventiveMaintenancePlan;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PreventiveMaintenancePlanRepository extends JpaRepository<PreventiveMaintenancePlan, Long> {
    List<PreventiveMaintenancePlan> findByActive(Boolean active);
}
