-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 228
-- File              : V228__create_platform_cloud_inventory.sql
-- Operation Type    : Schema Creation
-- Purpose           : create platform cloud inventory
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
-- V228: Cloud Resource Inventory DDL
CREATE TABLE IF NOT EXISTS platform_cloud_resource (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    resource_id         VARCHAR(250) NOT NULL UNIQUE,
    resource_type       VARCHAR(100) NOT NULL, -- VM, DB, REDIS, KAFKA, etc
    provider            VARCHAR(50) NOT NULL, -- AWS, AZURE, GCP
    region              VARCHAR(100) NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'RUNNING',
    cost_daily          NUMERIC(19,4) NOT NULL DEFAULT 0.00,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
