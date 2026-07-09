-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 165
-- File              : V165__create_grc_event_store.sql
-- Operation Type    : Schema Creation
-- Purpose           : create grc event store
--
-- Tables Created    : IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
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
