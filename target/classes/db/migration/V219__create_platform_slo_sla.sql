-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 219
-- File              : V219__create_platform_slo_sla.sql
-- Operation Type    : Schema Creation
-- Purpose           : create platform slo sla
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
-- V219: SLO/SLA DDL
CREATE TABLE IF NOT EXISTS platform_slo (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    name                VARCHAR(150) NOT NULL UNIQUE,
    target_percentage   NUMERIC(5,2) NOT NULL,
    service_name        VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_slo_measurement (
    id                  BIGSERIAL PRIMARY KEY,
    slo_id              BIGINT NOT NULL,
    current_percentage  NUMERIC(5,2) NOT NULL,
    error_budget        NUMERIC(5,2) NOT NULL,
    recorded_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
