package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.EmployeePayroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeePayrollRepository extends JpaRepository<EmployeePayroll, Long> {

    Optional<EmployeePayroll> findByPayrollPeriodIdAndEmployeeId(Long payrollPeriodId, Long employeeId);
}
