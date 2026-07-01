package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.EmployeeCompetency;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmployeeCompetencyRepository extends JpaRepository<EmployeeCompetency, Long> {
    List<EmployeeCompetency> findByEmployeeId(Long employeeId);
}
