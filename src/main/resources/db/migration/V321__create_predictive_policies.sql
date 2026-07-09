-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 321
-- File              : V321__create_predictive_policies.sql
-- Operation Type    : Schema Creation
-- Purpose           : create predictive policies
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
-- V321: Predictive Maintenance Policies
CREATE TABLE IF NOT EXISTS platform_predictive_maintenance_policy (
    id                              BIGSERIAL PRIMARY KEY,
    version                         INT NOT NULL DEFAULT 0,
    policy_code                     VARCHAR(100) NOT NULL UNIQUE,
    policy_name                     VARCHAR(200) NOT NULL,
    asset_type                      VARCHAR(100) NOT NULL,
    prediction_model                VARCHAR(100) NOT NULL,
    minimum_health_score            NUMERIC(5,2) NOT NULL,
    remaining_useful_life_threshold NUMERIC(10,2) NOT NULL,
    failure_probability_threshold   NUMERIC(5,2) NOT NULL,
    maintenance_strategy            VARCHAR(100) NOT NULL, -- Preventive, Predictive, Condition-Based
    priority                        INT NOT NULL DEFAULT 0,
    enabled                         BOOLEAN NOT NULL DEFAULT TRUE,
    created_by                      VARCHAR(100) NOT NULL,
    created_at                      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
