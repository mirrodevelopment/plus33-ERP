-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 158
-- File              : V158__create_compliance_framework.sql
-- Operation Type    : Schema Creation
-- Purpose           : create compliance framework
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
-- V158: Compliance Frameworks
CREATE TABLE IF NOT EXISTS grc_compliance_frameworks (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    description TEXT
);

CREATE TABLE IF NOT EXISTS grc_compliance_controls (
    id BIGSERIAL PRIMARY KEY,
    framework_id BIGINT NOT NULL,
    control_ref VARCHAR(50) NOT NULL,
    objective TEXT NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    owner_id BIGINT
);
