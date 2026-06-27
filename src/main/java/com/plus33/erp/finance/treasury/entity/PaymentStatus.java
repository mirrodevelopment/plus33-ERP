package com.plus33.erp.finance.treasury.entity;

/**
 * Exhaustive payment lifecycle status enum.
 * Transitions are enforced by {@link com.plus33.erp.finance.treasury.shared.PaymentLifecycleStateMachine}.
 *
 * Normal flow:
 *   DRAFT → VALIDATED → COMPLIANCE_PENDING → COMPLIANCE_APPROVED
 *         → APPROVAL_PENDING → APPROVED → QUEUED → TRANSMITTING
 *         → TRANSMITTED → BANK_ACCEPTED → SETTLED → RECONCILED
 *
 * Failure / exception paths:
 *   Any state → REJECTED | FAILED | CANCELLED | REVERSED | ON_HOLD | EXPIRED
 *   PARTIALLY_SETTLED if bank supports partial settlement
 */
public enum PaymentStatus {

    // ── Normal Lifecycle ───────────────────────────────────────────────────────
    DRAFT,
    VALIDATED,
    COMPLIANCE_PENDING,
    COMPLIANCE_APPROVED,
    APPROVAL_PENDING,
    APPROVED,
    QUEUED,
    TRANSMITTING,
    TRANSMITTED,
    BANK_ACCEPTED,
    SETTLED,
    RECONCILED,

    // ── Partial / Held ─────────────────────────────────────────────────────────
    PARTIALLY_SETTLED,
    ON_HOLD,

    // ── Terminal Failure States ────────────────────────────────────────────────
    REJECTED,
    FAILED,
    CANCELLED,
    REVERSED,
    EXPIRED;

    /** Returns true if the payment has reached a terminal state. */
    public boolean isTerminal() {
        return switch (this) {
            case RECONCILED, REJECTED, FAILED, CANCELLED, REVERSED, EXPIRED -> true;
            default -> false;
        };
    }

    /** Returns true if the payment is in an actionable in-flight state. */
    public boolean isInFlight() {
        return switch (this) {
            case TRANSMITTING, TRANSMITTED, BANK_ACCEPTED, PARTIALLY_SETTLED -> true;
            default -> false;
        };
    }
}
