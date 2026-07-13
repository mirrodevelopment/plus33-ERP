-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 150
-- File              : V150__create_workforce_planning.sql
-- Operation Type    : Schema Creation
-- Purpose           : create workforce planning
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
-- V150: Workforce Planning shift rosters
CREATE TABLE IF NOT EXISTS hcm_shift_patterns (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    weekly_hours NUMERIC(5, 2) NOT NULL DEFAULT 40.00
);

CREATE TABLE IF NOT EXISTS hcm_rosters (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    shift_date DATE NOT NULL,
    shift_pattern_id BIGINT NOT NULL
);
