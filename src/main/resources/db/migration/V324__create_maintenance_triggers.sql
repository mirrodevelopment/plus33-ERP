-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 324
-- File              : V324__create_maintenance_triggers.sql
-- Operation Type    : Schema Creation
-- Purpose           : create maintenance triggers
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
-- V324: Maintenance Triggers & Auto-Repair Logs
CREATE TABLE IF NOT EXISTS platform_maintenance_trigger_log (
    id                              BIGSERIAL PRIMARY KEY,
    trigger_source                  VARCHAR(100) NOT NULL, -- PREDICTIVE_ENGINE, MANUAL, THRESHOLD
    predicted_failure_id            BIGINT,
    work_order_reference            VARCHAR(100) NOT NULL,
    maintenance_status              VARCHAR(50) NOT NULL, -- SCHEDULED, ASSIGNED, COMPLETED, CANCELLED
    scheduled_time                  TIMESTAMP NOT NULL,
    completion_time                 TIMESTAMP,
    technician_assignment           VARCHAR(100),
    automatic_execution             BOOLEAN NOT NULL DEFAULT TRUE,
    rollback_status                 VARCHAR(50),
    triggered_at                    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
