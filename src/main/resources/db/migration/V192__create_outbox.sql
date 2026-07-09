-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 192
-- File              : V192__create_outbox.sql
-- Operation Type    : Schema Creation
-- Purpose           : create outbox
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
