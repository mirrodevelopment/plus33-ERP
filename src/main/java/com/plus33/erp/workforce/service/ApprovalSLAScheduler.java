package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.EmployeeLeave;
import com.plus33.erp.workforce.entity.LeaveApprovalWorkflow;
import com.plus33.erp.workforce.entity.LeaveAuditLog;
import com.plus33.erp.workforce.repository.EmployeeLeaveRepository;
import com.plus33.erp.workforce.repository.LeaveApprovalWorkflowRepository;
import com.plus33.erp.workforce.repository.LeaveAuditLogRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApprovalSLAScheduler {

    private final EmployeeLeaveRepository leaveRepository;
    private final LeaveApprovalWorkflowRepository workflowRepository;
    private final LeaveAuditLogRepository auditLogRepository;

    public ApprovalSLAScheduler(
            EmployeeLeaveRepository leaveRepository,
            LeaveApprovalWorkflowRepository workflowRepository,
            LeaveAuditLogRepository auditLogRepository) {
        this.leaveRepository = leaveRepository;
        this.workflowRepository = workflowRepository;
        this.auditLogRepository = auditLogRepository;
    }

    /**
     * Runs every hour to check for SLA breaches on pending leave requests.
     */
    @Scheduled(fixedDelay = 3600000)
    @Transactional
    public void checkSlaBreaches() {
        LocalDateTime now = LocalDateTime.now();

        // Query all pending leave requests
        List<EmployeeLeave> pending = leaveRepository.findByStatus("PENDING");

        for (EmployeeLeave el : pending) {
            if (el.getApprovalDueAt() != null && el.getApprovalDueAt().isBefore(now) && el.getEscalatedAt() == null) {
                // Determine escalation role from workflow configuration
                LeaveApprovalWorkflow workflow = workflowRepository
                        .findByCompanyIdAndLeaveTypeIdAndLevel(1L, el.getLeaveType().getId(), el.getCurrentApprovalLevel())
                        .orElse(null);

                if (workflow != null && workflow.getEscalateToRole() != null) {
                    // Escalate the request
                    el.setEscalatedAt(now);
                    el.setEscalatedTo(workflow.getEscalateToRole());
                    el.setApproverRole(workflow.getEscalateToRole()); // Assigns to escalated role queue
                    leaveRepository.save(el);

                    // Write audit log entry
                    LeaveAuditLog audit = new LeaveAuditLog();
                    audit.setLeave(el);
                    audit.setAction("ESCALATED");
                    audit.setNote("Leave request escalated to " + workflow.getEscalateToRole() + " due to SLA breach.");
                    auditLogRepository.save(audit);
                } else {
                    // If no escalation role, log reminder warning
                    LeaveAuditLog audit = new LeaveAuditLog();
                    audit.setLeave(el);
                    audit.setAction("SLA_REMINDER_SENT");
                    audit.setNote("SLA approval reminder warning triggered.");
                    auditLogRepository.save(audit);
                }
            }
        }
    }
}
