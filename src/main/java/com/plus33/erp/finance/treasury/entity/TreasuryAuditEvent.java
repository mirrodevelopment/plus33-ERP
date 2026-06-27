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

    @PrePersist
    protected void onCreate() {
        occurredAt = LocalDateTime.now();
    }
}
