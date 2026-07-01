package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.CompensationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CompensationHistoryRepository extends JpaRepository<CompensationHistory, Long> {
    List<CompensationHistory> findByEmployeeId(Long employeeId);
}
