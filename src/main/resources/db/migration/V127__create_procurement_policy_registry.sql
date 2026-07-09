-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 127
-- File              : V127__create_procurement_policy_registry.sql
-- Operation Type    : Schema Creation
-- Purpose           : create procurement policy registry
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
-- V127: Procurement Policy Registry
CREATE TABLE IF NOT EXISTS procurement_policies (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    policy_type VARCHAR(50) NOT NULL,
    threshold_amount NUMERIC(18, 2),
    preferred_supplier_id BIGINT,
    active BOOLEAN NOT NULL DEFAULT TRUE
);
