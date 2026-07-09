-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 213
-- File              : V213__create_resilience_circuit_breakers.sql
-- Operation Type    : Schema Creation
-- Purpose           : create resilience circuit breakers
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
-- V213: Resilience & Circuit Breakers DDL
CREATE TABLE IF NOT EXISTS platform_resilience_rule (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    service_name        VARCHAR(100) NOT NULL UNIQUE,
    rate_limit_rpm      INT NOT NULL DEFAULT 600,
    timeout_ms          INT NOT NULL DEFAULT 3000,
    retry_attempts      INT NOT NULL DEFAULT 3
);

CREATE TABLE IF NOT EXISTS platform_circuit_breaker_stats (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    breaker_name        VARCHAR(100) NOT NULL UNIQUE,
    status              VARCHAR(50) NOT NULL DEFAULT 'CLOSED', -- CLOSED, OPEN, HALF_OPEN
    failures_count      INT NOT NULL DEFAULT 0,
    success_ratio       NUMERIC(5,2) NOT NULL DEFAULT 100.00,
    recovery_time_sec   INT NOT NULL DEFAULT 60,
    last_trip_time      TIMESTAMP
);
