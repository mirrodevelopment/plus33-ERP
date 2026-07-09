package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.AttendanceAuditTrail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceAuditTrailRepository extends JpaRepository<AttendanceAuditTrail, Long> {

    List<AttendanceAuditTrail> findByAttendanceIdOrderByTimestampDesc(Long attendanceId);
}
