-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 341
-- File              : V341__create_dispatch_policies.sql
-- Operation Type    : Schema Creation
-- Purpose           : create dispatch policies
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
-- V341: AI Dispatch Policies
CREATE TABLE IF NOT EXISTS platform_dispatch_policy (
    id                          BIGSERIAL PRIMARY KEY,
    version                     INT NOT NULL DEFAULT 0,
    policy_code                 VARCHAR(100) NOT NULL UNIQUE,
    dispatch_strategy           VARCHAR(100) NOT NULL, -- NearestVehicle, LowestCost, CarbonOptimized
    priority_weight             NUMERIC(5,2) NOT NULL,
    vehicle_selection_strategy  VARCHAR(100) NOT NULL,
    driver_selection_strategy   VARCHAR(100) NOT NULL,
    optimization_goal           VARCHAR(100) NOT NULL,
    planning_horizon_mins       INT NOT NULL,
    allow_partial_load          BOOLEAN NOT NULL DEFAULT FALSE,
    allow_split_delivery        BOOLEAN NOT NULL DEFAULT FALSE,
    allow_dynamic_reroute       BOOLEAN NOT NULL DEFAULT TRUE,
    enabled                     BOOLEAN NOT NULL DEFAULT TRUE,
    created_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
