package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.HcmGoal;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HcmGoalRepository extends JpaRepository<HcmGoal, Long> {
    List<HcmGoal> findByEmployeeId(Long employeeId);
}
