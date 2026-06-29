package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.AttendanceSyncLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttendanceSyncLogRepository extends JpaRepository<AttendanceSyncLog, Long> {
    List<AttendanceSyncLog> findByCompanyIdAndEmployeeId(Long companyId, Long employeeId);
}
