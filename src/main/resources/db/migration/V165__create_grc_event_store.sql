-- V165: GRC Event Store
CREATE TABLE IF NOT EXISTS grc_event_store (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    event_type VARCHAR(100) NOT NULL,
    payload_json TEXT NOT NULL,
    event_version VARCHAR(20) NOT NULL DEFAULT '1.0',
    schema_version VARCHAR(20) NOT NULL DEFAULT '1.0',
    correlation_id VARCHAR(100),
    causation_id VARCHAR(100),
    idempotency_key VARCHAR(100) UNIQUE,
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
