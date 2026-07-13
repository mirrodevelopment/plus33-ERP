-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 148
-- File              : V148__create_succession_planning.sql
-- Operation Type    : Schema Creation
-- Purpose           : create succession planning
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
-- V148: Succession Planning
CREATE TABLE IF NOT EXISTS hcm_talent_pools (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS hcm_successors (
    id BIGSERIAL PRIMARY KEY,
    talent_pool_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    readiness_score NUMERIC(5, 2) NOT NULL DEFAULT 0.00
);
