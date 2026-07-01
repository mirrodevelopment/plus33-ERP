-- V207: Telemetry DDL
CREATE TABLE IF NOT EXISTS platform_telemetry_metric (
    id                  BIGSERIAL PRIMARY KEY,
    metric_name         VARCHAR(100) NOT NULL,
    metric_value        NUMERIC(19,4) NOT NULL,
    dimensions_json     TEXT,
    timestamp           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
