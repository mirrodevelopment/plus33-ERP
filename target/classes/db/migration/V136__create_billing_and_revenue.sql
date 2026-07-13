-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 136
-- File              : V136__create_billing_and_revenue.sql
-- Operation Type    : Schema Creation
-- Purpose           : create billing and revenue
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
-- V136: Project Contracts and Billing milestones
CREATE TABLE IF NOT EXISTS project_billing_contracts (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL UNIQUE,
    contract_type VARCHAR(30) NOT NULL DEFAULT 'TIME_AND_MATERIAL',
    billing_amount NUMERIC(18, 2) NOT NULL DEFAULT 0,
    recognized_revenue NUMERIC(18, 2) NOT NULL DEFAULT 0,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT'
);

CREATE TABLE IF NOT EXISTS project_billing_milestones (
    id BIGSERIAL PRIMARY KEY,
    contract_id BIGINT NOT NULL,
    milestone_name VARCHAR(100) NOT NULL,
    milestone_percentage NUMERIC(5, 2) NOT NULL,
    amount NUMERIC(18, 2) NOT NULL,
    billed BOOLEAN NOT NULL DEFAULT FALSE
);
