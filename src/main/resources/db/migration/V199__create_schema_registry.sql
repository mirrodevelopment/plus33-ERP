-- V199: Schema Registry, Locks & Metrics DDL
CREATE TABLE IF NOT EXISTS integration_schema_registry (
    id                  BIGSERIAL PRIMARY KEY,
    event_type          VARCHAR(250) NOT NULL,
    schema_version      VARCHAR(50) NOT NULL,
    schema_definition   TEXT NOT NULL,
    compatibility_rule  VARCHAR(50) NOT NULL DEFAULT 'BACKWARD',
    deprecation_date    TIMESTAMP,
    replacement_event   VARCHAR(250),
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(event_type, schema_version)
);

CREATE TABLE IF NOT EXISTS integration_monitoring_metric (
    id                  BIGSERIAL PRIMARY KEY,
    metric_name         VARCHAR(100) NOT NULL,
    metric_value        NUMERIC(19,4) NOT NULL,
    dimensions_json     TEXT,
    timestamp           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS integration_lock (
    lock_key            VARCHAR(250) PRIMARY KEY,
    owner_id            VARCHAR(250) NOT NULL,
    expires_at          TIMESTAMP NOT NULL
);
