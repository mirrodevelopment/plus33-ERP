-- V244: Telemetry & anomaly thresholds
CREATE TABLE IF NOT EXISTS platform_twin_telemetry (
    id                  BIGSERIAL PRIMARY KEY,
    instance_id         BIGINT NOT NULL,
    metric_name         VARCHAR(100) NOT NULL,
    metric_value        NUMERIC(19,4) NOT NULL,
    recorded_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_twin_anomaly_threshold (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    instance_id         BIGINT NOT NULL,
    metric_name         VARCHAR(100) NOT NULL,
    min_value           NUMERIC(19,4) NOT NULL,
    max_value           NUMERIC(19,4) NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_twin_alert (
    id                  BIGSERIAL PRIMARY KEY,
    instance_id         BIGINT NOT NULL,
    alert_type          VARCHAR(100) NOT NULL,
    severity            VARCHAR(50) NOT NULL DEFAULT 'WARNING',
    message             TEXT NOT NULL,
    triggered_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
