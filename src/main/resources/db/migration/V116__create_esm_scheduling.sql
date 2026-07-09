-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 116
-- File              : V116__create_esm_scheduling.sql
-- Operation Type    : Schema Creation
-- Purpose           : create esm scheduling
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
-- V116: Workforce Scheduling, Technicians, and Skill Certifications
CREATE TABLE IF NOT EXISTS esm_technician_skills (
    id BIGSERIAL PRIMARY KEY,
    technician_id BIGINT NOT NULL,
    skill_code VARCHAR(50) NOT NULL,
    proficiency_level VARCHAR(20) NOT NULL,
    UNIQUE(technician_id, skill_code)
);
