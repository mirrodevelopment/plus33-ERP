-- V344: Dispatch Constraint Checks
CREATE TABLE IF NOT EXISTS platform_dispatch_constraint_check (
    id                          BIGSERIAL PRIMARY KEY,
    dispatch_id                 BIGINT NOT NULL,
    constraint_type             VARCHAR(100) NOT NULL, -- Capacity, ShiftLimits, DeliveryWindows
    status                      VARCHAR(50) NOT NULL, -- PASSED, VIOLATED
    reason                      VARCHAR(500),
    severity                    VARCHAR(50) NOT NULL, -- INFO, WARNING, CRITICAL
    checked_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
