-- V217: Observability DDL
CREATE TABLE IF NOT EXISTS platform_trace_span (
    id                  BIGSERIAL PRIMARY KEY,
    trace_id            VARCHAR(100) NOT NULL,
    span_id             VARCHAR(100) NOT NULL,
    parent_span_id      VARCHAR(100),
    operation_name      VARCHAR(250) NOT NULL,
    duration_ms         BIGINT NOT NULL,
    timestamp           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_anomaly_alert (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    alert_name          VARCHAR(150) NOT NULL,
    severity            VARCHAR(50) NOT NULL DEFAULT 'WARNING', -- WARNING, CRITICAL
    status              VARCHAR(50) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, RESOLVED
    trigger_message     TEXT NOT NULL,
    timestamp           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
