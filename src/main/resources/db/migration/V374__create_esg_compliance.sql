-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 374
-- File              : V374__create_esg_compliance.sql
-- Operation Type    : Schema Creation
-- Purpose           : create esg compliance
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
-- V374: ESG Compliance Logs
CREATE TABLE IF NOT EXISTS platform_esg_compliance_log (
    id                          BIGSERIAL PRIMARY KEY,
    framework                   VARCHAR(100) NOT NULL, -- GRI, SASB, CSRD
    compliance_score            NUMERIC(5,2) NOT NULL,
    status                      VARCHAR(50) NOT NULL, -- COMPLIANT, WARNING, NON_COMPLIANT
    finding_summary             TEXT NOT NULL,
    corrective_action           TEXT NOT NULL,
    owner_name                  VARCHAR(100) NOT NULL,
    next_review_date            TIMESTAMP NOT NULL,
    audited_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
