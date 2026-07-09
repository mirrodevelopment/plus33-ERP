package com.plus33.erp.workforce.entity;

import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Entity @Table(name = "leave_approval_history")
@NoArgsConstructor @AllArgsConstructor
public class LeaveApprovalHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_id", nullable = false)
    private EmployeeLeave leave;

    /** Approval level this record corresponds to */
    @Column(name = "level", nullable = false)
    private Integer level = 1;

    /** NULL for SYSTEM approvals */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_user_id")
    private User approverUser;

    @Column(name = "approver_role", length = 30)
    private String approverRole;

    /** APPROVED / REJECTED / ESCALATED / PENDING */
    @Column(name = "decision", nullable = false, length = 20)
    private String decision = "PENDING";

    @Column(name = "supervisor_comment", columnDefinition = "TEXT")
    private String supervisorComment;

    @Column(name = "store_admin_comment", columnDefinition = "TEXT")
    private String storeAdminComment;

    @Column(name = "hr_comment", columnDefinition = "TEXT")
    private String hrComment;

    @Column(name = "old_status", length = 30)
    private String oldStatus;

    @Column(name = "new_status", length = 30)
    private String newStatus;

    @Column(name = "approval_due_at")
    private LocalDateTime approvalDueAt;

    @Column(name = "escalated_at")
    private LocalDateTime escalatedAt;

    @Column(name = "escalated_to", length = 50)
    private String escalatedTo;

    @Column(name = "decided_at")
    private LocalDateTime decidedAt;

    @Column(name = "session_id", length = 100)
    private String sessionId;

    @Column(name = "device_info", length = 200)
    private String deviceInfo;
}
