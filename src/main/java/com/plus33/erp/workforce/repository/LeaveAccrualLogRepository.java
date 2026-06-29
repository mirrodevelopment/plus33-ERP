package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.LeaveAccrualLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveAccrualLogRepository extends JpaRepository<LeaveAccrualLog, Long> {
    List<LeaveAccrualLog> findByCompanyIdAndEmployeeId(Long companyId, Long employeeId);
}
