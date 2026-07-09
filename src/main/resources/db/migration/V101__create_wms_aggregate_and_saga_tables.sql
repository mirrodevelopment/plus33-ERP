-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 101
-- File              : V101__create_wms_aggregate_and_saga_tables.sql
-- Operation Type    : Schema Creation
-- Purpose           : create wms aggregate and saga tables
--
-- Tables Created    : IF, IF, IF, IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V101: Enterprise Event Store, Inventory Snapshots, Saga Tracking, Retention & Universal Document References
CREATE TABLE IF NOT EXISTS warehouse_event_store (
    id BIGSERIAL PRIMARY KEY,
    aggregate_type VARCHAR(50) NOT NULL,
    aggregate_id BIGINT NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    payload_json TEXT NOT NULL,
    version_number BIGINT NOT NULL DEFAULT 0,
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    processed BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS inventory_snapshots (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    lot_number VARCHAR(50),
    serial_number VARCHAR(100),
    snapshot_date DATE NOT NULL,
    available_quantity NUMERIC(18, 6) NOT NULL DEFAULT 0,
    reserved_quantity NUMERIC(18, 6) NOT NULL DEFAULT 0,
    allocated_quantity NUMERIC(18, 6) NOT NULL DEFAULT 0,
    on_hand_quantity NUMERIC(18, 6) NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS warehouse_saga_states (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    saga_type VARCHAR(50) NOT NULL,
    correlation_id VARCHAR(100) NOT NULL UNIQUE,
    current_step VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'IN_PROGRESS',
    payload_json TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS warehouse_retention_policies (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    entity_name VARCHAR(50) NOT NULL UNIQUE,
    archive_after_days INT NOT NULL DEFAULT 365,
    purge_after_days INT NOT NULL DEFAULT 730,
    compression_policy VARCHAR(30) DEFAULT 'STANDARD'
);

CREATE TABLE IF NOT EXISTS document_references (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    document_type VARCHAR(30) NOT NULL,
    document_number VARCHAR(100) NOT NULL,
    document_id BIGINT NOT NULL,
    module VARCHAR(30) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
