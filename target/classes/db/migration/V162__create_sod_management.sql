-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 162
-- File              : V162__create_sod_management.sql
-- Operation Type    : Schema Creation
-- Purpose           : create sod management
--
-- Tables Created    : IF, IF, IF, IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V162: SoD & Access Reviews
CREATE TABLE IF NOT EXISTS grc_sod_rules (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    rule_name VARCHAR(150) NOT NULL,
    role_a VARCHAR(100) NOT NULL,
    role_b VARCHAR(100) NOT NULL,
    risk_level VARCHAR(20) NOT NULL DEFAULT 'HIGH',
    sod_type VARCHAR(20) NOT NULL DEFAULT 'PREVENTIVE'
);

CREATE TABLE IF NOT EXISTS grc_sod_violations (
    id BIGSERIAL PRIMARY KEY,
    sod_rule_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    detected_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN'
);

CREATE TABLE IF NOT EXISTS grc_sod_exemptions (
    id BIGSERIAL PRIMARY KEY,
    violation_id BIGINT NOT NULL,
    approved_by_id BIGINT NOT NULL,
    expiry_date DATE NOT NULL,
    justification TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS grc_access_review_campaigns (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    campaign_name VARCHAR(150) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PLANNED',
    start_date DATE NOT NULL,
    end_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS grc_access_review_decisions (
    id BIGSERIAL PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    role_name VARCHAR(100) NOT NULL,
    decision VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    reviewer_id BIGINT,
    decided_at TIMESTAMP
);
