-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 134
-- File              : V134__create_budgets_and_forecasts.sql
-- Operation Type    : Schema Creation
-- Purpose           : create budgets and forecasts
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
-- V134: Project Budgeting and Forecasts
CREATE TABLE IF NOT EXISTS project_budgets (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    current_version INT NOT NULL DEFAULT 1,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT'
);

CREATE TABLE IF NOT EXISTS project_budget_versions (
    id BIGSERIAL PRIMARY KEY,
    budget_id BIGINT NOT NULL,
    version_number INT NOT NULL,
    original_budget_amount NUMERIC(18, 2) NOT NULL DEFAULT 0,
    revised_budget_amount NUMERIC(18, 2) NOT NULL DEFAULT 0,
    forecast_amount NUMERIC(18, 2) NOT NULL DEFAULT 0,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    UNIQUE(budget_id, version_number)
);
