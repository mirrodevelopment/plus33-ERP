-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 156
-- File              : V156__create_audit_engagement.sql
-- Operation Type    : Schema Creation
-- Purpose           : create audit engagement
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
-- V156: Audit Engagements & Findings
CREATE TABLE IF NOT EXISTS grc_audit_engagements (
    id BIGSERIAL PRIMARY KEY,
    program_id BIGINT NOT NULL,
    audit_universe_id BIGINT NOT NULL,
    engagement_number VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PLANNED',
    lead_auditor_id BIGINT,
    start_date DATE,
    end_date DATE
);

CREATE TABLE IF NOT EXISTS grc_audit_findings (
    id BIGSERIAL PRIMARY KEY,
    engagement_id BIGINT NOT NULL,
    finding_number VARCHAR(50) NOT NULL UNIQUE,
    title VARCHAR(200) NOT NULL,
    severity VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    description TEXT
);

CREATE TABLE IF NOT EXISTS grc_audit_finding_responses (
    id BIGSERIAL PRIMARY KEY,
    finding_id BIGINT NOT NULL,
    management_response TEXT NOT NULL,
    agreed_remediation_date DATE NOT NULL,
    responder_id BIGINT,
    responded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
