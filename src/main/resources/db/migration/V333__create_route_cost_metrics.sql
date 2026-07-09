-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 333
-- File              : V333__create_route_cost_metrics.sql
-- Operation Type    : Schema Creation
-- Purpose           : create route cost metrics
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
-- V333: Route Cost Metrics
CREATE TABLE IF NOT EXISTS platform_route_cost_log (
    id                      BIGSERIAL PRIMARY KEY,
    route_id                BIGINT NOT NULL,
    fuel_cost               NUMERIC(12,2) NOT NULL,
    driver_cost             NUMERIC(12,2) NOT NULL,
    maintenance_cost        NUMERIC(12,2) NOT NULL,
    toll_cost               NUMERIC(12,2) NOT NULL,
    parking_cost            NUMERIC(12,2) NOT NULL,
    insurance_cost          NUMERIC(12,2) NOT NULL,
    depreciation_cost       NUMERIC(12,2) NOT NULL,
    carbon_cost             NUMERIC(12,2) NOT NULL,
    total_cost              NUMERIC(12,2) NOT NULL,
    currency                VARCHAR(3) NOT NULL DEFAULT 'USD',
    logged_at               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
