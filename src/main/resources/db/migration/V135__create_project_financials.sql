-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 135
-- File              : V135__create_project_financials.sql
-- Operation Type    : Schema Creation
-- Purpose           : create project financials
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
-- V135: Project Costing and Financial Ledger
CREATE TABLE IF NOT EXISTS project_cost_ledger (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    task_id BIGINT,
    cost_type VARCHAR(30) NOT NULL, -- LABOR, MATERIAL, OVERHEAD, etc.
    amount NUMERIC(18, 2) NOT NULL,
    source_module VARCHAR(50),
    source_id BIGINT,
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
