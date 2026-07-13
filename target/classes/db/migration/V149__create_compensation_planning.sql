-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 149
-- File              : V149__create_compensation_planning.sql
-- Operation Type    : Schema Creation
-- Purpose           : create compensation planning
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
-- V149: Compensation Planning and Salaries History
CREATE TABLE IF NOT EXISTS hcm_compensation_history (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    effective_date DATE NOT NULL,
    base_salary NUMERIC(18, 2) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
