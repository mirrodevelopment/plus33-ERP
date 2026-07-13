-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 196
-- File              : V196__create_api_gateway.sql
-- Operation Type    : Schema Creation
-- Purpose           : create api gateway
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
-- V196: API Gateway DDL
CREATE TABLE IF NOT EXISTS integration_gateway_key (
    id                  BIGSERIAL PRIMARY KEY,
    api_key             VARCHAR(100) NOT NULL UNIQUE,
    partner_code        VARCHAR(100) NOT NULL,
    rate_limit_per_min  INT NOT NULL DEFAULT 60,
    quota_per_day       INT NOT NULL DEFAULT 10000,
    allowed_routes      VARCHAR(500) NOT NULL DEFAULT '*',
    active              BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS integration_gateway_usage_log (
    id                  BIGSERIAL PRIMARY KEY,
    api_key             VARCHAR(100) NOT NULL,
    request_path        VARCHAR(250) NOT NULL,
    http_method         VARCHAR(10) NOT NULL,
    status_code         INT NOT NULL,
    response_time_ms    BIGINT NOT NULL,
    called_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
