package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payroll_approval_workflows")
public class PayrollApprovalWorkflow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payroll_run_id", nullable = false)
    private Long payrollRunId;

    @Column(name = "step_number", nullable = false)
    private Integer stepNumber;

    @Column(name = "approver_role", nullable = false, length = 50)
    private String approverRole;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";

    private String comments;

    @Column(name = "actioned_at")
    private LocalDateTime actionedAt;

    public PayrollApprovalWorkflow() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getPayrollRunId() { return payrollRunId; }
    public void setPayrollRunId(Long payrollRunId) { this.payrollRunId = payrollRunId; }
    public Integer getStepNumber() { return stepNumber; }
    public void setStepNumber(Integer stepNumber) { this.stepNumber = stepNumber; }
    public String getApproverRole() { return approverRole; }
    public void setApproverRole(String approverRole) { this.approverRole = approverRole; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
    public LocalDateTime getActionedAt() { return actionedAt; }
    public void setActionedAt(LocalDateTime actionedAt) { this.actionedAt = actionedAt; }
}
