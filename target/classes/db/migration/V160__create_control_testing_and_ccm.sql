-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 160
-- File              : V160__create_control_testing_and_ccm.sql
-- Operation Type    : Schema Creation
-- Purpose           : create control testing and ccm
--
-- Tables Created    : IF, IF, IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V160: Control Testing & Continuous Control Monitoring
CREATE TABLE IF NOT EXISTS grc_control_test_plans (
    id BIGSERIAL PRIMARY KEY,
    control_library_id BIGINT NOT NULL,
    test_name VARCHAR(150) NOT NULL,
    frequency VARCHAR(30) NOT NULL DEFAULT 'QUARTERLY',
    next_test_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS grc_control_test_results (
    id BIGSERIAL PRIMARY KEY,
    test_plan_id BIGINT NOT NULL,
    result VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    tested_by_id BIGINT,
    tested_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notes TEXT
);

CREATE TABLE IF NOT EXISTS grc_continuous_control_monitors (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    monitor_name VARCHAR(150) NOT NULL,
    monitor_type VARCHAR(50) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS grc_control_alerts (
    id BIGSERIAL PRIMARY KEY,
    monitor_id BIGINT NOT NULL,
    alert_message TEXT NOT NULL,
    severity VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    status VARCHAR(20) NOT NULL DEFAULT 'OPEN',
    raised_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
