-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 257
-- File              : V257__create_optimization_strategies.sql
-- Operation Type    : Schema Creation
-- Purpose           : create optimization strategies
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
-- V257: Optimization Strategies
CREATE TABLE IF NOT EXISTS platform_optimization_strategy (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    strategy_code       VARCHAR(100) NOT NULL UNIQUE,
    strategy_name       VARCHAR(150) NOT NULL,
    parameters_json     TEXT NOT NULL,
    active              BOOLEAN NOT NULL DEFAULT TRUE
);
