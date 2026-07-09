/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : EmployeeLeave.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeLeaveController
 * Related Service   : EmployeeLeaveService, EmployeeLeaveServiceImpl
 * Related Repository: EmployeeLeaveRepository
 * Related Entity    : EmployeeLeave
 * Related DTO       : N/A
 * Related Mapper    : EmployeeLeaveMapper
 * Related DB Table  : employee_leaves
 * Related REST APIs : N/A
 * Depends On        : Security Module
 * Used By           : EmployeeLeaveRepository, EmployeeLeaveMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'employee_leaves'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeLeave}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'employee_leaves'.</p>
 *
 * <p><b>Database Table   :</b> {@code employee_leaves}</p>
 * <p><b>Module Deps      :</b> Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "employee_leaves")
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLeave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "total_days", nullable = false, precision = 5, scale = 2)
    private BigDecimal totalDays;

    @Column(columnDefinition = "TEXT")
    private String reason;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "rejection_reason", columnDefinition = "TEXT")
    private String rejectionReason;

    /** Leave session: FULL_DAY / HALF_DAY_AM / HALF_DAY_PM */
    @Column(name = "leave_session", length = 20)
    private String leaveSession = "FULL_DAY";

    /** Role of the approver who made the final decision */
    @Column(name = "approver_role", length = 30)
    private String approverRole;

    /** Current level in the approval workflow */
    @Column(name = "current_approval_level")
    private Integer currentApprovalLevel = 1;

    /** Timestamp when cancelled */
    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    /** TRUE when employee requests cancellation of an APPROVED leave */
    @Column(name = "cancellation_requested")
    private Boolean cancellationRequested = false;

    /** Reason provided by employee when cancelling (min 10 chars) */
    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    /** SLA deadline for approval before reminder is sent */
    @Column(name = "approval_due_at")
    private LocalDateTime approvalDueAt;

    /** Timestamp when the request was escalated due to SLA breach */
    @Column(name = "escalated_at")
    private LocalDateTime escalatedAt;

    /** Role that the escalation was sent to */
    @Column(name = "escalated_to", length = 50)
    private String escalatedTo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}