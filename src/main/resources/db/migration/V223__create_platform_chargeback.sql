-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 223
-- File              : V223__create_platform_chargeback.sql
-- Operation Type    : Schema Creation
-- Purpose           : create platform chargeback
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
-- V223: Chargeback DDL
CREATE TABLE IF NOT EXISTS platform_cost_center (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    center_code         VARCHAR(100) NOT NULL UNIQUE,
    center_name         VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_cost_allocation (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    cost_center_id      BIGINT NOT NULL,
    resource_id         VARCHAR(250) NOT NULL,
    allocation_ratio    NUMERIC(5,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_chargeback (
    id                  BIGSERIAL PRIMARY KEY,
    cost_center_id      BIGINT NOT NULL,
    amount              NUMERIC(19,4) NOT NULL,
    billing_month       VARCHAR(20) NOT NULL,
    recorded_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
