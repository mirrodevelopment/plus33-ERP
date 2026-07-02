-- V355: Fuel Audits Logs
CREATE TABLE IF NOT EXISTS platform_fuel_audit_log (
    id                          BIGSERIAL PRIMARY KEY,
    policy_version              INT NOT NULL,
    optimization_algorithm      VARCHAR(100) NOT NULL,
    operator                    VARCHAR(100) NOT NULL,
    approval_status             VARCHAR(50) NOT NULL,
    execution_time_ms           BIGINT NOT NULL,
    previous_configuration      TEXT NOT NULL,
    new_configuration           TEXT NOT NULL,
    trace_id                    VARCHAR(100) NOT NULL,
    audited_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
