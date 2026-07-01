-- V194: Broker Registry DDL
CREATE TABLE IF NOT EXISTS bi_broker_registry (
    id                  BIGSERIAL PRIMARY KEY,
    broker_name         VARCHAR(100) NOT NULL UNIQUE,
    broker_type         VARCHAR(50) NOT NULL, -- e.g. KAFKA, RABBITMQ, IN_MEMORY
    connection_url      VARCHAR(250) NOT NULL,
    active              BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
