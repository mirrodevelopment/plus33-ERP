package com.plus33.erp.workforce.entity;

import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p>Entity representing an employee's request to leave the store geofence
 * (beyond 200 m) during an active shift without triggering auto clock-out.</p>
 *
 * <p>Flow:
 * <ol>
 *   <li>Employee submits request (status = PENDING)</li>
 *   <li>Shift Supervisor or Store Admin approves with a custom duration (status = APPROVED,
 *       approvedUntil = now + approvedDurationMins)</li>
 *   <li>Backend monitors: after approvedUntil + gracePeriodMins (10 min), if employee is still
 *       outside geofence → auto clock-out, status = EXPIRED</li>
 *   <li>Before auto clock-out fires, employee may request an extension (status = EXTENSION_REQUESTED)</li>
 * </ol>
 * </p>
 *
 * <p><b>Database Table:</b> {@code away_permission_requests}</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "away_permission_requests")
@NoArgsConstructor
@AllArgsConstructor
public class AwayPermissionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The employee who submitted the request. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    /** The store from which the employee wishes to leave. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    /** The active attendance record during which this request was made. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    /** When the request was submitted. */
    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    /** When the request was resolved (approved or denied). */
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    /** The supervisor/admin who approved or denied. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resolved_by")
    private User resolvedBy;

    /**
     * Current lifecycle status.
     * Values: PENDING | APPROVED | DENIED | EXPIRED | EXTENSION_REQUESTED
     */
    @Column(name = "status", nullable = false, length = 30)
    private String status = "PENDING";

    /** Optional reason provided by the employee. */
    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    /** Duration granted by the supervisor in minutes. Set when status = APPROVED. */
    @Column(name = "approved_duration_mins")
    private Integer approvedDurationMins;

    /**
     * Absolute deadline: requestedAt + approvedDurationMins.
     * The geofence monitor gives a 10-minute grace period beyond this before auto clock-out.
     */
    @Column(name = "approved_until")
    private LocalDateTime approvedUntil;

    /**
     * Grace period in minutes after approvedUntil before auto clock-out fires.
     * Default: 10 minutes. Allows employee time to return.
     */
    @Column(name = "grace_buffer_mins", nullable = false)
    private Integer graceBufferMins = 10;

    /** When the employee requested an extension (during grace period). */
    @Column(name = "extension_requested_at")
    private LocalDateTime extensionRequestedAt;

    /** Reason for extension request. */
    @Column(name = "extension_reason", columnDefinition = "TEXT")
    private String extensionReason;

    /** If this is an extension of a previous away pass, link to the parent. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_request_id")
    private AwayPermissionRequest parentRequest;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (requestedAt == null) requestedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Returns true if the away pass is currently valid (approved and not past grace deadline).
     */
    public boolean isActive() {
        if (!"APPROVED".equals(status) && !"EXTENSION_REQUESTED".equals(status)) return false;
        if (approvedUntil == null) return false;
        LocalDateTime graceDeadline = approvedUntil.plusMinutes(graceBufferMins != null ? graceBufferMins : 10);
        return LocalDateTime.now().isBefore(graceDeadline);
    }

    /**
     * Returns true when the pass has expired but the grace period is still active
     * (employee can request an extension).
     */
    public boolean isInGracePeriod() {
        if (!"APPROVED".equals(status)) return false;
        if (approvedUntil == null) return false;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime graceDeadline = approvedUntil.plusMinutes(graceBufferMins != null ? graceBufferMins : 10);
        return now.isAfter(approvedUntil) && now.isBefore(graceDeadline);
    }
}
