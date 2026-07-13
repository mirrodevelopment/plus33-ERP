-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 342
-- File              : V342__create_dispatch_assignments.sql
-- Operation Type    : Schema Creation
-- Purpose           : create dispatch assignments
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
-- V342: Dispatch Assignments
CREATE TABLE IF NOT EXISTS platform_dispatch_assignment (
    id                          BIGSERIAL PRIMARY KEY,
    dispatch_code               VARCHAR(100) NOT NULL UNIQUE,
    vehicle_id                  BIGINT NOT NULL,
    driver_id                   BIGINT NOT NULL,
    route_id                    BIGINT NOT NULL,
    shipment_id                 BIGINT NOT NULL,
    assignment_status           VARCHAR(50) NOT NULL, -- CREATED, ASSIGNED, ACCEPTED, EN_ROUTE, DELIVERED, FAILED
    assigned_time               TIMESTAMP NOT NULL,
    accepted_time               TIMESTAMP,
    completed_time              TIMESTAMP,
    estimated_eta               TIMESTAMP NOT NULL,
    actual_eta                  TIMESTAMP
);
