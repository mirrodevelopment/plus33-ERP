-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 344
-- File              : V344__create_dispatch_constraint_checks.sql
-- Operation Type    : Schema Creation
-- Purpose           : create dispatch constraint checks
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
-- V344: Dispatch Constraint Checks
CREATE TABLE IF NOT EXISTS platform_dispatch_constraint_check (
    id                          BIGSERIAL PRIMARY KEY,
    dispatch_id                 BIGINT NOT NULL,
    constraint_type             VARCHAR(100) NOT NULL, -- Capacity, ShiftLimits, DeliveryWindows
    status                      VARCHAR(50) NOT NULL, -- PASSED, VIOLATED
    reason                      VARCHAR(500),
    severity                    VARCHAR(50) NOT NULL, -- INFO, WARNING, CRITICAL
    checked_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
