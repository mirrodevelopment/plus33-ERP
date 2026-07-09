-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 264
-- File              : V264__create_autonomous_rerouting.sql
-- Operation Type    : Schema Creation
-- Purpose           : create autonomous rerouting
--
-- Tables Created    : IF, IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V264: Autonomous Rerouting
CREATE TABLE IF NOT EXISTS platform_rerouting_policy (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    policy_code         VARCHAR(100) NOT NULL UNIQUE,
    trigger_threshold_minutes INT NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_autonomous_rerouting (
    id                  BIGSERIAL PRIMARY KEY,
    transit_route_id    BIGINT NOT NULL,
    policy_id           BIGINT NOT NULL,
    confidence          NUMERIC(5,2) NOT NULL,
    suggested_route_json TEXT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'PENDING'
);

CREATE TABLE IF NOT EXISTS platform_rerouting_execution (
    id                  BIGSERIAL PRIMARY KEY,
    rerouting_id        BIGINT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'EXECUTED',
    executed_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
