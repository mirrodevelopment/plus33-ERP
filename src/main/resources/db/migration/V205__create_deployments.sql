-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 205
-- File              : V205__create_deployments.sql
-- Operation Type    : Schema Creation
-- Purpose           : create deployments
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
-- V205: Deployments DDL
CREATE TABLE IF NOT EXISTS platform_deployment_group (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    group_name          VARCHAR(100) NOT NULL UNIQUE,
    active_router       BOOLEAN NOT NULL DEFAULT FALSE,
    canary_weight       INT NOT NULL DEFAULT 0,
    target_version      VARCHAR(100),
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_deployment_history (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    deployment_version  VARCHAR(100) NOT NULL,
    status              VARCHAR(50) NOT NULL,
    changelog           TEXT,
    deployed_by         VARCHAR(100) NOT NULL,
    started_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at        TIMESTAMP
);
