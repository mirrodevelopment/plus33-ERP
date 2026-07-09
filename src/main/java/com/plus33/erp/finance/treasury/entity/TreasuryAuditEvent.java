/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : TreasuryAuditEvent.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryAuditEventController
 * Related Service   : TreasuryAuditEventService, TreasuryAuditEventServiceImpl
 * Related Repository: TreasuryAuditEventRepository
 * Related Entity    : TreasuryAuditEvent
 * Related DTO       : N/A
 * Related Mapper    : TreasuryAuditEventMapper
 * Related DB Table  : treasury_audit_events
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : TreasuryAuditEventRepository, TreasuryAuditEventMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'treasury_audit_events'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Immutable audit event log for every treasury action.
 * Acts as the audit trail / event sourcing log for the treasury bounded context.
 */
@Getter
@Entity
@Table(name = "treasury_audit_events", indexes = {
    @Index(name = "idx_tae_aggregate", columnList = "aggregate_type, aggregate_id"),
    @Index(name = "idx_tae_actor", columnList = "actor, occurred_at"),
    @Index(name = "idx_tae_company", columnList = "company_id, occurred_at")
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryAuditEvent}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'treasury_audit_events'.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_audit_events}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class TreasuryAuditEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    /** Domain aggregate type — e.g. CashTransfer, TreasuryInvestment, PaymentBatch */
    @Column(name = "aggregate_type", nullable = false, length = 80)
    private String aggregateType;

    /** Primary key of the aggregate */
    @Column(name = "aggregate_id", nullable = false)
    private Long aggregateId;

    /** Action performed — e.g. CREATED, STATUS_CHANGED, APPROVED, JOURNAL_POSTED */
    @Column(name = "event_action", nullable = false, length = 80)
    private String eventAction;

    /** State before the action (JSON or string representation) */
    @Column(name = "previous_state", columnDefinition = "TEXT")
    private String previousState;

    /** State after the action */
    @Column(name = "new_state", columnDefinition = "TEXT")
    private String newState;

    /** Human-readable description of the event */
    @Column(name = "description", length = 500)
    private String description;

    /** User who triggered this event */
    @Column(nullable = false, length = 100)
    private String actor;

    /** IP address or system identifier of the actor */
    @Column(name = "actor_context", length = 200)
    private String actorContext;

    @Column(name = "occurred_at", nullable = false, updatable = false)
    private LocalDateTime occurredAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        occurredAt = LocalDateTime.now();
    }
}