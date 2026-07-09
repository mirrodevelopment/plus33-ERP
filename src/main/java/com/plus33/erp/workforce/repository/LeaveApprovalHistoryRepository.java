package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.LeaveApprovalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface LeaveApprovalHistoryRepository extends JpaRepository<LeaveApprovalHistory, Long> {

    List<LeaveApprovalHistory> findByLeaveIdOrderByLevelAsc(Long leaveId);

    Optional<LeaveApprovalHistory> findByLeaveIdAndLevel(Long leaveId, Integer level);
}
