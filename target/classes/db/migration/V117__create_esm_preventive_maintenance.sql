-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 117
-- File              : V117__create_esm_preventive_maintenance.sql
-- Operation Type    : Schema Creation
-- Purpose           : create esm preventive maintenance
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
-- V117: Preventive Maintenance Plans and IoT Device Alarms
CREATE TABLE IF NOT EXISTS esm_pm_plans (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    installed_asset_id BIGINT NOT NULL,
    interval_days INT NOT NULL,
    next_service_date DATE NOT NULL,
    trigger_type VARCHAR(30) NOT NULL DEFAULT 'CALENDAR',
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS esm_iot_alarms (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    device_id VARCHAR(100) NOT NULL,
    alarm_type VARCHAR(50) NOT NULL,
    metric_value NUMERIC(12, 4),
    severity VARCHAR(20) NOT NULL,
    processed BOOLEAN NOT NULL DEFAULT FALSE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
