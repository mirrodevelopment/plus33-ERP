package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.EmployeeLeave;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeLeaveRepository extends JpaRepository<EmployeeLeave, Long> {

    List<EmployeeLeave> findByEmployeeId(Long employeeId);

    List<EmployeeLeave> findByStatus(String status);
}
