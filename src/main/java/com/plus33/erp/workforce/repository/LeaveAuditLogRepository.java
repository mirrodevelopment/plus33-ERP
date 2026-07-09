package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.LeaveAuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LeaveAuditLogRepository extends JpaRepository<LeaveAuditLog, Long> {

    List<LeaveAuditLog> findByLeaveIdOrderByCreatedAtDesc(Long leaveId);

    List<LeaveAuditLog> findByActorUserIdOrderByCreatedAtDesc(Long actorUserId);
}
