package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.EmployeeLifecycle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmployeeLifecycleRepository extends JpaRepository<EmployeeLifecycle, Long> {
    Optional<EmployeeLifecycle> findByEmployeeId(Long employeeId);
}
