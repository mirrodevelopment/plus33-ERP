-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 303
-- File              : V303__create_device_diagnostics.sql
-- Operation Type    : Schema Creation
-- Purpose           : create device diagnostics
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
-- V303: Device Diagnostics & Crash Reports
CREATE TABLE IF NOT EXISTS platform_device_diagnostic (
    id                      BIGSERIAL PRIMARY KEY,
    node_id                 BIGINT NOT NULL,
    cpu_usage               NUMERIC(5,2),
    memory_usage            NUMERIC(5,2),
    disk_usage              NUMERIC(5,2),
    temperature             NUMERIC(5,2),
    running_services        TEXT,
    firmware_version        VARCHAR(50),
    uptime_seconds          BIGINT,
    network_quality         VARCHAR(50),
    logs                    TEXT,
    stack_trace             TEXT,
    exception_message       VARCHAR(500),
    thread_dump             TEXT,
    core_dump_location      VARCHAR(500),
    reported_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
