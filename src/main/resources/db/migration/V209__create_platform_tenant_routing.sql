-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 209
-- File              : V209__create_platform_tenant_routing.sql
-- Operation Type    : Schema Creation
-- Purpose           : create platform tenant routing
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
-- V209: Tenant Routing DDL
CREATE TABLE IF NOT EXISTS platform_tenant_routing (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    tenant_id           VARCHAR(50) NOT NULL UNIQUE,
    region              VARCHAR(100) NOT NULL,
    routing_policy      VARCHAR(100) NOT NULL DEFAULT 'ACTIVE_ACTIVE', -- e.g. ACTIVE_ACTIVE, ACTIVE_PASSIVE, REGION_AFFINITY, DISASTER_RECOVERY
    replica_url         VARCHAR(250),
    healthy             BOOLEAN NOT NULL DEFAULT TRUE,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
