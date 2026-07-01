-- V195: Connector Framework DDL
CREATE TABLE IF NOT EXISTS integration_connector (
    id                  BIGSERIAL PRIMARY KEY,
    connector_code      VARCHAR(100) NOT NULL UNIQUE,
    connector_type      VARCHAR(50) NOT NULL,
    host                VARCHAR(250),
    port                INT,
    credential_reference VARCHAR(250),
    status              VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    health_status       VARCHAR(50) NOT NULL DEFAULT 'HEALTHY',
    last_heartbeat      TIMESTAMP,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS integration_connector_execution (
    id                  BIGSERIAL PRIMARY KEY,
    connector_code      VARCHAR(100) NOT NULL,
    status              VARCHAR(50) NOT NULL,
    started_at          TIMESTAMP NOT NULL,
    completed_at        TIMESTAMP NOT NULL,
    payload_sent        TEXT,
    payload_received    TEXT,
    error_message       TEXT
);
