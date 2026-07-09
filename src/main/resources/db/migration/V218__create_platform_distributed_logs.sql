-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 218
-- File              : V218__create_platform_distributed_logs.sql
-- Operation Type    : Schema Creation
-- Purpose           : create platform distributed logs
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
-- V218: Distributed Logs DDL
CREATE TABLE IF NOT EXISTS platform_log_entry (
    id                  BIGSERIAL PRIMARY KEY,
    trace_id            VARCHAR(100),
    span_id             VARCHAR(100),
    service_name        VARCHAR(100) NOT NULL,
    node_id             VARCHAR(100) NOT NULL,
    log_level           VARCHAR(50) NOT NULL,
    logger              VARCHAR(250) NOT NULL,
    message             TEXT NOT NULL,
    json_payload        TEXT,
    timestamp           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
