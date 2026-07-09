package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.LeaveApprovalWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LeaveApprovalWorkflowRepository extends JpaRepository<LeaveApprovalWorkflow, Long> {

    Optional<LeaveApprovalWorkflow> findByCompanyIdAndLeaveTypeIdAndLevel(
            Long companyId, Long leaveTypeId, Integer level);
}
