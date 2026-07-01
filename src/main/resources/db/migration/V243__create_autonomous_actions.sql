-- V243: Autonomous actions
CREATE TABLE IF NOT EXISTS platform_autonomous_action (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    action_code         VARCHAR(100) NOT NULL UNIQUE,
    description         TEXT NOT NULL,
    active              BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS platform_autonomous_execution (
    id                  BIGSERIAL PRIMARY KEY,
    action_id           BIGINT NOT NULL,
    confidence_score    NUMERIC(5,2) NOT NULL,
    decision_policy     VARCHAR(100) NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'PENDING', -- PENDING_APPROVAL, EXECUTED, REJECTED
    operator_notes      TEXT,
    executed_at         TIMESTAMP
);
