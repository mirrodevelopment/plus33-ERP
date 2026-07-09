-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 212
-- File              : V212__create_autoscaling_policies.sql
-- Operation Type    : Schema Creation
-- Purpose           : create autoscaling policies
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
-- V212: Autoscaling Policies DDL
CREATE TABLE IF NOT EXISTS platform_scaling_policy (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    metric_name         VARCHAR(100) NOT NULL UNIQUE,
    threshold_value     NUMERIC(19,4) NOT NULL,
    min_replicas        INT NOT NULL DEFAULT 1,
    max_replicas        INT NOT NULL DEFAULT 10,
    cooldown_seconds    INT NOT NULL DEFAULT 300
);

CREATE TABLE IF NOT EXISTS platform_scaling_activity (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    activity_type       VARCHAR(50) NOT NULL, -- SCALE_UP, SCALE_DOWN
    current_replicas    INT NOT NULL,
    desired_replicas    INT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'COMPLETED',
    started_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    completed_at        TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_scaling_decision (
    id                  BIGSERIAL PRIMARY KEY,
    timestamp           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    metric_name         VARCHAR(100) NOT NULL,
    current_value       NUMERIC(19,4) NOT NULL,
    threshold_value     NUMERIC(19,4) NOT NULL,
    current_replicas    INT NOT NULL,
    desired_replicas    INT NOT NULL,
    reason              TEXT NOT NULL
);
