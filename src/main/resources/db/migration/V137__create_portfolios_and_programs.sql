-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 137
-- File              : V137__create_portfolios_and_programs.sql
-- Operation Type    : Schema Creation
-- Purpose           : create portfolios and programs
--
-- Tables Created    : IF, IF
-- Tables Altered    : projects
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V137: Portfolios and Programs Governance
CREATE TABLE IF NOT EXISTS project_portfolios (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS project_programs (
    id BIGSERIAL PRIMARY KEY,
    portfolio_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL
);

ALTER TABLE projects ADD COLUMN IF NOT EXISTS program_id BIGINT;
