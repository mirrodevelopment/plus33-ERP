-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 161
-- File              : V161__create_regulatory_obligations.sql
-- Operation Type    : Schema Creation
-- Purpose           : create regulatory obligations
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
-- V161: Regulatory Obligations & Compliance Calendar
CREATE TABLE IF NOT EXISTS grc_regulatory_obligations (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    regulation_name VARCHAR(150) NOT NULL,
    description TEXT,
    frequency VARCHAR(30) NOT NULL DEFAULT 'ANNUAL',
    owner_id BIGINT
);

CREATE TABLE IF NOT EXISTS grc_regulatory_submissions (
    id BIGSERIAL PRIMARY KEY,
    obligation_id BIGINT NOT NULL,
    submission_date DATE NOT NULL,
    due_date DATE NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    submitted_by_id BIGINT
);

CREATE TABLE IF NOT EXISTS grc_compliance_milestones (
    id BIGSERIAL PRIMARY KEY,
    obligation_id BIGINT NOT NULL,
    milestone_name VARCHAR(150) NOT NULL,
    due_date DATE NOT NULL,
    completed BOOLEAN NOT NULL DEFAULT FALSE
);
