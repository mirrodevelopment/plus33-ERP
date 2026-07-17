package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.EmployeeShiftSwap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeShiftSwapRepository extends JpaRepository<EmployeeShiftSwap, Long> {

    List<EmployeeShiftSwap> findByEmployeeIdOrderByCreatedAtDesc(Long employeeId);

    List<EmployeeShiftSwap> findByEmployeeIdInAndStatusOrderByCreatedAtDesc(List<Long> employeeIds, String status);

    boolean existsByEmployeeIdAndShiftDateAndStatus(Long employeeId, LocalDate shiftDate, String status);
}
