-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 138
-- File              : V138__create_project_risk_and_change_management.sql
-- Operation Type    : Schema Creation
-- Purpose           : create project risk and change management
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
-- V138: Project Risks and Change Management
CREATE TABLE IF NOT EXISTS project_risks (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    probability VARCHAR(20) NOT NULL DEFAULT 'LOW',
    impact VARCHAR(20) NOT NULL DEFAULT 'LOW',
    mitigation_plan TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS project_change_requests (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    request_number VARCHAR(50) NOT NULL UNIQUE,
    change_type VARCHAR(30) NOT NULL, -- SCOPE, BUDGET, SCHEDULE
    impact_analysis TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING'
);
