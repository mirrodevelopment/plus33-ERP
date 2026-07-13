-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 245
-- File              : V245__create_conformance_rules.sql
-- Operation Type    : Schema Creation
-- Purpose           : create conformance rules
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
-- V245: Conformance rules & SLA tracking
CREATE TABLE IF NOT EXISTS platform_conformance_rule (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    process_name        VARCHAR(150) NOT NULL,
    expected_activity   VARCHAR(150) NOT NULL,
    sequence_order      INT NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_conformance_deviation (
    id                  BIGSERIAL PRIMARY KEY,
    case_id             BIGINT NOT NULL,
    rule_id             BIGINT NOT NULL,
    deviation_details   TEXT NOT NULL,
    sla_breach_risk     BOOLEAN NOT NULL DEFAULT FALSE,
    detected_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
