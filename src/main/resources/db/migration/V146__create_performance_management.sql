-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 146
-- File              : V146__create_performance_management.sql
-- Operation Type    : Schema Creation
-- Purpose           : create performance management
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
-- V146: Performance and Competency Management
CREATE TABLE IF NOT EXISTS hcm_goals (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    target_date DATE NOT NULL,
    progress_percentage NUMERIC(5, 2) NOT NULL DEFAULT 0.00,
    status VARCHAR(30) NOT NULL DEFAULT 'IN_PROGRESS'
);

CREATE TABLE IF NOT EXISTS hcm_competencies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS hcm_employee_competencies (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    competency_id BIGINT NOT NULL,
    rating NUMERIC(3, 2) NOT NULL DEFAULT 0.00
);
