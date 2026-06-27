package com.plus33.erp.finance.treasury.shared;

import com.plus33.erp.finance.treasury.entity.PaymentStatus;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Pre-wired state machine for the payment lifecycle.
 * Delegates to the generic {@link StateMachine} utility, enforcing the
 * approved V29 transition matrix.
 *
 * <pre>
 * DRAFT → VALIDATED → COMPLIANCE_PENDING → COMPLIANCE_APPROVED
 *       → APPROVAL_PENDING → APPROVED → QUEUED → TRANSMITTING
 *       → TRANSMITTED → BANK_ACCEPTED → SETTLED → RECONCILED
 *
 * From any non-terminal state:
 *   → CANCELLED
 *   → ON_HOLD (reversible to previous via HOLD_RELEASED event)
 *   → EXPIRED (time-based)
 *
 * From TRANSMITTED / BANK_ACCEPTED:
 *   → FAILED, REJECTED, PARTIALLY_SETTLED
 *
 * From SETTLED:
 *   → REVERSED
 * </pre>
 */
public final class PaymentLifecycleStateMachine {

    private static final StateMachine<PaymentStatus> INSTANCE;

    static {
        Map<PaymentStatus, Set<PaymentStatus>> matrix = new EnumMap<>(PaymentStatus.class);

        matrix.put(PaymentStatus.DRAFT, EnumSet.of(
                PaymentStatus.VALIDATED,
                PaymentStatus.CANCELLED,
                PaymentStatus.EXPIRED));

        matrix.put(PaymentStatus.VALIDATED, EnumSet.of(
                PaymentStatus.COMPLIANCE_PENDING,
                PaymentStatus.APPROVAL_PENDING, // bypass compliance if not required
                PaymentStatus.ON_HOLD,
                PaymentStatus.CANCELLED,
                PaymentStatus.EXPIRED));

        matrix.put(PaymentStatus.COMPLIANCE_PENDING, EnumSet.of(
                PaymentStatus.COMPLIANCE_APPROVED,
                PaymentStatus.REJECTED,
                PaymentStatus.ON_HOLD,
                PaymentStatus.CANCELLED,
                PaymentStatus.EXPIRED));

        matrix.put(PaymentStatus.COMPLIANCE_APPROVED, EnumSet.of(
                PaymentStatus.APPROVAL_PENDING,
                PaymentStatus.APPROVED, // auto-approve if no manual step
                PaymentStatus.ON_HOLD,
                PaymentStatus.CANCELLED));

        matrix.put(PaymentStatus.APPROVAL_PENDING, EnumSet.of(
                PaymentStatus.APPROVED,
                PaymentStatus.REJECTED,
                PaymentStatus.ON_HOLD,
                PaymentStatus.CANCELLED,
                PaymentStatus.EXPIRED));

        matrix.put(PaymentStatus.APPROVED, EnumSet.of(
                PaymentStatus.QUEUED,
                PaymentStatus.ON_HOLD,
                PaymentStatus.CANCELLED));

        matrix.put(PaymentStatus.QUEUED, EnumSet.of(
                PaymentStatus.TRANSMITTING,
                PaymentStatus.ON_HOLD,
                PaymentStatus.CANCELLED,
                PaymentStatus.EXPIRED));

        matrix.put(PaymentStatus.TRANSMITTING, EnumSet.of(
                PaymentStatus.TRANSMITTED,
                PaymentStatus.FAILED,
                PaymentStatus.ON_HOLD));

        matrix.put(PaymentStatus.TRANSMITTED, EnumSet.of(
                PaymentStatus.BANK_ACCEPTED,
                PaymentStatus.REJECTED,
                PaymentStatus.FAILED,
                PaymentStatus.PARTIALLY_SETTLED));

        matrix.put(PaymentStatus.BANK_ACCEPTED, EnumSet.of(
                PaymentStatus.SETTLED,
                PaymentStatus.PARTIALLY_SETTLED,
                PaymentStatus.FAILED));

        matrix.put(PaymentStatus.PARTIALLY_SETTLED, EnumSet.of(
                PaymentStatus.SETTLED,
                PaymentStatus.FAILED,
                PaymentStatus.REVERSED));

        matrix.put(PaymentStatus.SETTLED, EnumSet.of(
                PaymentStatus.RECONCILED,
                PaymentStatus.REVERSED));

        matrix.put(PaymentStatus.ON_HOLD, EnumSet.of(
                PaymentStatus.VALIDATED,        // re-enter where left off
                PaymentStatus.COMPLIANCE_PENDING,
                PaymentStatus.APPROVAL_PENDING,
                PaymentStatus.APPROVED,
                PaymentStatus.QUEUED,
                PaymentStatus.CANCELLED,
                PaymentStatus.EXPIRED));

        // Terminal states have no outgoing transitions
        for (PaymentStatus terminal : new PaymentStatus[]{
                PaymentStatus.RECONCILED, PaymentStatus.REJECTED, PaymentStatus.FAILED,
                PaymentStatus.CANCELLED, PaymentStatus.REVERSED, PaymentStatus.EXPIRED}) {
            matrix.put(terminal, EnumSet.noneOf(PaymentStatus.class));
        }

        INSTANCE = new StateMachine<>(matrix);
    }

    private PaymentLifecycleStateMachine() {}

    /**
     * Validates that transitioning from {@code current} to {@code target} is permitted.
     *
     * @throws IllegalStateException if the transition is invalid
     */
    public static void assertValidTransition(PaymentStatus current, PaymentStatus target) {
        if (!INSTANCE.validateTransition(current, target)) {
            throw new IllegalStateException(
                    "Invalid payment status transition: " + current + " → " + target);
        }
    }

    /** Returns true if the transition is permitted without throwing. */
    public static boolean isValidTransition(PaymentStatus current, PaymentStatus target) {
        return INSTANCE.validateTransition(current, target);
    }
}
