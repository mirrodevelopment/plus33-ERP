-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 229
-- File              : V229__create_platform_cost_optimization.sql
-- Operation Type    : Schema Creation
-- Purpose           : create platform cost optimization
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
-- V229: Cost Optimization Recommendations DDL
CREATE TABLE IF NOT EXISTS platform_cost_recommendation (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    resource_id         VARCHAR(250) NOT NULL,
    recommendation_type VARCHAR(100) NOT NULL, -- RIGHTSIZE, TERMINATE, PURCHASE_RI
    savings_potential   NUMERIC(19,4) NOT NULL,
    reason              TEXT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, APPLIED, DISMISSED
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
