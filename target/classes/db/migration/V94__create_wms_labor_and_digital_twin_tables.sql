-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 94
-- File              : V94__create_wms_labor_and_digital_twin_tables.sql
-- Operation Type    : Schema Creation
-- Purpose           : create wms labor and digital twin tables
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
-- V94: Labor Management, Digital Twin Spatial Graph & Slotting Tables
CREATE TABLE IF NOT EXISTS warehouse_labor_logs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    task_id BIGINT,
    task_type VARCHAR(30) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP,
    travel_time_seconds INT DEFAULT 0,
    idle_time_seconds INT DEFAULT 0,
    items_handled NUMERIC(18, 6) DEFAULT 0,
    labor_cost NUMERIC(18, 2) DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS warehouse_nodes (
    id BIGSERIAL PRIMARY KEY,
    warehouse_id BIGINT NOT NULL,
    location_id BIGINT UNIQUE,
    node_code VARCHAR(50) NOT NULL,
    x_coord NUMERIC(10, 2) NOT NULL DEFAULT 0,
    y_coord NUMERIC(10, 2) NOT NULL DEFAULT 0,
    z_coord NUMERIC(10, 2) NOT NULL DEFAULT 0,
    temperature_class VARCHAR(20) DEFAULT 'AMBIENT',
    accessibility_flags VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS warehouse_edges (
    id BIGSERIAL PRIMARY KEY,
    warehouse_id BIGINT NOT NULL,
    from_node_id BIGINT NOT NULL REFERENCES warehouse_nodes(id),
    to_node_id BIGINT NOT NULL REFERENCES warehouse_nodes(id),
    distance_meters NUMERIC(10, 2) NOT NULL,
    bidirectional BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS slotting_recommendations (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    current_location_id BIGINT,
    recommended_location_id BIGINT NOT NULL,
    reason VARCHAR(255) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
