-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 194
-- File              : V194__create_broker_registry.sql
-- Operation Type    : Schema Creation
-- Purpose           : create broker registry
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
