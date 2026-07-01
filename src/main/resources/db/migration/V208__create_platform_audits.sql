-- V208: Platform Audits DDL
CREATE TABLE IF NOT EXISTS platform_audit_log (
    id                  BIGSERIAL PRIMARY KEY,
    action_name         VARCHAR(100) NOT NULL,
    user_identity       VARCHAR(100) NOT NULL,
    trace_context       VARCHAR(250),
    payload_diff        TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
