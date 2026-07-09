-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 133
-- File              : V133__create_resources_and_timesheets.sql
-- Operation Type    : Schema Creation
-- Purpose           : create resources and timesheets
--
-- Tables Created    : IF, IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V133: Project Resources and Timesheets
CREATE TABLE IF NOT EXISTS project_resources (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    capacity_hours_per_week NUMERIC(5, 2) NOT NULL DEFAULT 40.00
);

CREATE TABLE IF NOT EXISTS project_resource_assignments (
    id BIGSERIAL PRIMARY KEY,
    project_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL,
    resource_id BIGINT NOT NULL,
    allocation_percentage NUMERIC(5, 2) NOT NULL DEFAULT 100.00,
    start_date DATE,
    end_date DATE
);

CREATE TABLE IF NOT EXISTS project_timesheets (
    id BIGSERIAL PRIMARY KEY,
    resource_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL,
    work_date DATE NOT NULL,
    hours_worked NUMERIC(5, 2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'SUBMITTED'
);
