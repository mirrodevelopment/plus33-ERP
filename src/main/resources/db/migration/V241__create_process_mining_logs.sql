-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 241
-- File              : V241__create_process_mining_logs.sql
-- Operation Type    : Schema Creation
-- Purpose           : create process mining logs
--
-- Tables Created    : IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V241: Process mining event logs
CREATE TABLE IF NOT EXISTS platform_process_case (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    case_token          VARCHAR(100) NOT NULL UNIQUE,
    process_name        VARCHAR(150) NOT NULL,
    active              BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_process_event_log (
    id                  BIGSERIAL PRIMARY KEY,
    case_id             BIGINT NOT NULL,
    activity_name       VARCHAR(150) NOT NULL,
    transition_state    VARCHAR(50) NOT NULL,
    duration_ms         BIGINT NOT NULL,
    recorded_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
