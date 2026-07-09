/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : LeaveType.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LeaveTypeController
 * Related Service   : LeaveTypeService, LeaveTypeServiceImpl
 * Related Repository: LeaveTypeRepository
 * Related Entity    : LeaveType
 * Related DTO       : N/A
 * Related Mapper    : LeaveTypeMapper
 * Related DB Table  : leave_types
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : LeaveTypeRepository, LeaveTypeMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'leave_types'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code LeaveType}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'leave_types'.</p>
 *
 * <p><b>Database Table   :</b> {@code leave_types}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "leave_types")
@NoArgsConstructor
@AllArgsConstructor
public class LeaveType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Boolean paid = true;

    @Column(name = "annual_limit")
    private Integer annualLimit;

    @Column(name = "carry_forward", nullable = false)
    private Boolean carryForward = false;

    @Column(nullable = false)
    private Boolean active = true;

    /** Approval workflow role: SUPERVISOR / STORE_ADMIN / SYSTEM */
    @Column(name = "approval_level", length = 30)
    private String approvalLevel = "SUPERVISOR";

    /** TRUE for Bereavement, Maternity, Paternity — cannot be rejected */
    @Column(name = "protected", nullable = false)
    private Boolean protectedLeave = false;

    /** TRUE if a document must be uploaded before approval */
    @Column(name = "requires_document", nullable = false)
    private Boolean requiresDocument = false;

    /** Monthly accrual in days — only used for Annual Leave (2.5) */
    @Column(name = "monthly_accrual", precision = 5, scale = 2)
    private java.math.BigDecimal monthlyAccrual;

    /** Maximum consecutive working days allowed per request */
    @Column(name = "max_consecutive_days")
    private Integer maxConsecutiveDays;

    /** Minimum notice days required before leave starts */
    @Column(name = "min_notice_days")
    private Integer minNoticeDays = 0;

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