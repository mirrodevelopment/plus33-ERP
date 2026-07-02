-- V365: Energy Audits Logs
CREATE TABLE IF NOT EXISTS platform_ev_energy_audit_log (
    id                          BIGSERIAL PRIMARY KEY,
    optimization_algorithm      VARCHAR(100) NOT NULL,
    energy_before_kwh           NUMERIC(10,2) NOT NULL,
    energy_after_kwh            NUMERIC(10,2) NOT NULL,
    estimated_cost              NUMERIC(12,2) NOT NULL,
    estimated_savings           NUMERIC(12,2) NOT NULL,
    carbon_offset_kg            NUMERIC(10,2) NOT NULL,
    operator                    VARCHAR(100) NOT NULL,
    trace_id                    VARCHAR(100) NOT NULL,
    execution_duration_ms       BIGINT NOT NULL,
    audited_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
