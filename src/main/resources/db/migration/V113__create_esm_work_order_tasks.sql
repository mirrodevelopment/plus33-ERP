-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 113
-- File              : V113__create_esm_work_order_tasks.sql
-- Operation Type    : Schema Creation
-- Purpose           : create esm work order tasks
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
-- V113: Work Order Tasks, Checklists, and technician notes
CREATE TABLE IF NOT EXISTS esm_work_order_tasks (
    id BIGSERIAL PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    task_sequence INT NOT NULL,
    task_description VARCHAR(255) NOT NULL,
    estimated_minutes INT NOT NULL,
    actual_minutes INT,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    required_skill VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS esm_checklists (
    id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL,
    item_description VARCHAR(255) NOT NULL,
    checked BOOLEAN NOT NULL DEFAULT FALSE
);
