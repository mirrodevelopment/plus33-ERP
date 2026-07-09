-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 96
-- File              : V96__create_wms_equipment_and_robotics_tables.sql
-- Operation Type    : Schema Creation
-- Purpose           : create wms equipment and robotics tables
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
-- V96: Equipment Assets, Maintenance & Autonomous Robotics Tables
CREATE TABLE IF NOT EXISTS equipment_assets (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    asset_code VARCHAR(50) NOT NULL UNIQUE,
    equipment_type VARCHAR(30) NOT NULL, -- FORKLIFT, REACH_TRUCK, AGV, AMR, DRONE, CONVEYOR
    battery_level_pct INT DEFAULT 100,
    status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE',
    last_maintenance_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS robot_tasks (
    id BIGSERIAL PRIMARY KEY,
    equipment_id BIGINT REFERENCES equipment_assets(id),
    warehouse_task_id BIGINT NOT NULL,
    robot_provider_key VARCHAR(50) NOT NULL, -- AGV_MAIN, AMR_FETCH, DRONE_SCAN
    payload_json TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'DISPATCHED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
