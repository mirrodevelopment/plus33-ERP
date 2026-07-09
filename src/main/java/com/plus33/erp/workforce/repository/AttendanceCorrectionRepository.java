package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.AttendanceCorrection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceCorrectionRepository extends JpaRepository<AttendanceCorrection, Long> {

    List<AttendanceCorrection> findByEmployeeIdOrderByRequestDateDesc(Long employeeId);
}
