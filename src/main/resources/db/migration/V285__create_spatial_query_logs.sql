-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 285
-- File              : V285__create_spatial_query_logs.sql
-- Operation Type    : Schema Creation
-- Purpose           : create spatial query logs
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
-- V285: Spatial Query Logs
CREATE TABLE IF NOT EXISTS platform_spatial_query_log (
    id                  BIGSERIAL PRIMARY KEY,
    executed_query      TEXT NOT NULL,
    execution_time_ms   BIGINT NOT NULL,
    returned_rows       INT NOT NULL,
    spatial_index_used  VARCHAR(100),
    bounding_box_wkt    TEXT,
    query_type          VARCHAR(100) NOT NULL, -- BBOX, RADIUS, POLYGON_CONTAINMENT
    queried_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
