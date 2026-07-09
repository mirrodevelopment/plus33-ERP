package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.EmployeeLeaveYearSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EmployeeLeaveYearSummaryRepository extends JpaRepository<EmployeeLeaveYearSummary, Long> {

    Optional<EmployeeLeaveYearSummary> findByEmployeeIdAndYear(Long employeeId, Integer year);
}
