package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.EmployeeLeaveBalance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface EmployeeLeaveBalanceRepository extends JpaRepository<EmployeeLeaveBalance, Long> {

    Optional<EmployeeLeaveBalance> findByEmployeeIdAndLeaveTypeIdAndYear(
            Long employeeId, Long leaveTypeId, Integer year);

    List<EmployeeLeaveBalance> findByEmployeeIdAndYear(Long employeeId, Integer year);

    List<EmployeeLeaveBalance> findByLeaveTypeIdAndYear(Long leaveTypeId, Integer year);
}
