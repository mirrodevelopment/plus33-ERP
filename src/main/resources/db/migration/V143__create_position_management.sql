-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 143
-- File              : V143__create_position_management.sql
-- Operation Type    : Schema Creation
-- Purpose           : create position management
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
-- V143: Position Management
CREATE TABLE IF NOT EXISTS hcm_positions (
    id BIGSERIAL PRIMARY KEY,
    department_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(100) NOT NULL,
    effective_from DATE NOT NULL,
    effective_to DATE NOT NULL,
    version_number INT NOT NULL DEFAULT 1,
    is_current BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS hcm_position_assignments (
    id BIGSERIAL PRIMARY KEY,
    position_id BIGINT NOT NULL,
    employee_id BIGINT,
    effective_from DATE NOT NULL,
    effective_to DATE NOT NULL,
    is_current BOOLEAN NOT NULL DEFAULT TRUE
);
