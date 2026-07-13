-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 222
-- File              : V222__create_platform_finops.sql
-- Operation Type    : Schema Creation
-- Purpose           : create platform finops
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
-- V222: FinOps DDL
CREATE TABLE IF NOT EXISTS platform_finops_budget (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    budget_name         VARCHAR(150) NOT NULL UNIQUE,
    limit_amount        NUMERIC(19,4) NOT NULL,
    alert_threshold     NUMERIC(5,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_cloud_cost_item (
    id                  BIGSERIAL PRIMARY KEY,
    resource_id         VARCHAR(250) NOT NULL,
    cost                NUMERIC(19,4) NOT NULL,
    billing_period      VARCHAR(50) NOT NULL,
    recorded_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
