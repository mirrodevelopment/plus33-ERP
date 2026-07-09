package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Entity @Table(name = "leave_approval_workflows")
@NoArgsConstructor @AllArgsConstructor
public class LeaveApprovalWorkflow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId = 1L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    /** Approval level: 1, 2, 3... */
    @Column(name = "level", nullable = false)
    private Integer level = 1;

    /** Role that must approve at this level: SUPERVISOR / STORE_ADMIN / SYSTEM */
    @Column(name = "approver_role", nullable = false, length = 50)
    private String approverRole;

    /** Hours before a reminder notification is sent */
    @Column(name = "sla_hours")
    private Integer slaHours = 24;

    /** Hours before the request is escalated to the next role */
    @Column(name = "escalation_hours")
    private Integer escalationHours = 48;

    /** Role to escalate to if SLA is breached */
    @Column(name = "escalate_to_role", length = 50)
    private String escalateToRole;

    @Column(name = "can_skip")
    private Boolean canSkip = false;

    @Column(name = "is_required")
    private Boolean isRequired = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate  protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
