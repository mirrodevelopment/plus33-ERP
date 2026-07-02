-- V345: Dispatch Audits Logs
CREATE TABLE IF NOT EXISTS platform_dispatch_audit_log (
    id                          BIGSERIAL PRIMARY KEY,
    optimizer_version           VARCHAR(50) NOT NULL,
    planning_time_ms            BIGINT NOT NULL,
    decision_trace              TEXT NOT NULL,
    rollback_reference          VARCHAR(100),
    execution_id                VARCHAR(100) NOT NULL,
    audited_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
