-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 164
-- File              : V164__create_evidence_vault.sql
-- Operation Type    : Schema Creation
-- Purpose           : create evidence vault
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
-- V164: Compliance Evidence Vault
CREATE TABLE IF NOT EXISTS grc_compliance_evidence (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    reference_type VARCHAR(50) NOT NULL,
    reference_id BIGINT NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    content_hash VARCHAR(64) NOT NULL UNIQUE,
    evidence_source VARCHAR(100),
    uploaded_by_id BIGINT NOT NULL,
    verified_by_id BIGINT,
    verification_date DATE,
    retention_policy VARCHAR(50) NOT NULL DEFAULT '7_YEARS',
    uploaded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
