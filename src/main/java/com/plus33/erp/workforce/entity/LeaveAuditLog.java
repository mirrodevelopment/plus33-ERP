package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Getter @Setter @Entity @Table(name = "leave_audit_log")
@NoArgsConstructor @AllArgsConstructor
public class LeaveAuditLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_id")
    private EmployeeLeave leave;

    /** NULL for SYSTEM-initiated actions such as accruals */
    @Column(name = "actor_user_id")
    private Long actorUserId;

    /**
     * Action codes:
     * SUBMITTED, APPROVED, REJECTED, CANCELLED, DOCUMENT_UPLOADED,
     * ACCRUED, CARRY_FORWARDED, ENCASHED, ADJUSTED, EXPIRED,
     * BLACKOUT_BLOCKED, SHIFT_CONFLICT_WARNED,
     * CANCELLATION_REQUESTED, CANCELLATION_APPROVED,
     * SLA_REMINDER_SENT, ESCALATED, SYSTEM_AUTO_APPROVED
     */
    @Column(name = "action", nullable = false, length = 50)
    private String action;

    /** JSON snapshot of entity fields before the change */
    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue;

    /** JSON snapshot of entity fields after the change */
    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue;

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;

    @Column(name = "ip_address", length = 50)
    private String ipAddress;

    @Column(name = "session_id", length = 100)
    private String sessionId;

    @Column(name = "device_info", length = 200)
    private String deviceInfo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist protected void onCreate() { createdAt = LocalDateTime.now(); }
}
