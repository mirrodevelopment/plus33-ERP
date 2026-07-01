-- V192: Outbox schema DDL
CREATE TABLE IF NOT EXISTS integration_outbox (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT DEFAULT 0,
    event_id            VARCHAR(100) NOT NULL UNIQUE,
    event_type          VARCHAR(250) NOT NULL,
    topic               VARCHAR(250) NOT NULL,
    payload             TEXT NOT NULL,
    trace_parent        VARCHAR(250),
    correlation_id      VARCHAR(100),
    tenant_id           VARCHAR(50),
    status              VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    attempts            INT NOT NULL DEFAULT 0,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
