-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 334
-- File              : V334__create_routing_recommendations.sql
-- Operation Type    : Schema Creation
-- Purpose           : create routing recommendations
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
-- V334: Optimization Recommendations
CREATE TABLE IF NOT EXISTS platform_routing_optimization_recommendation (
    id                      BIGSERIAL PRIMARY KEY,
    recommended_route       TEXT NOT NULL,
    estimated_savings_cost  NUMERIC(12,2) NOT NULL,
    estimated_time_saved_m  INT NOT NULL,
    estimated_fuel_saved_l  NUMERIC(10,2) NOT NULL,
    estimated_co2_saved_kg  NUMERIC(10,2) NOT NULL,
    confidence_score        NUMERIC(5,2) NOT NULL,
    algorithm_version       VARCHAR(50) NOT NULL,
    accepted                BOOLEAN NOT NULL DEFAULT FALSE,
    implemented             BOOLEAN NOT NULL DEFAULT FALSE,
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
