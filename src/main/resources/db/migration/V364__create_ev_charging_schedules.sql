-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 364
-- File              : V364__create_ev_charging_schedules.sql
-- Operation Type    : Schema Creation
-- Purpose           : create ev charging schedules
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
-- V364: Charging Schedules
CREATE TABLE IF NOT EXISTS platform_ev_charging_schedule (
    id                          BIGSERIAL PRIMARY KEY,
    vehicle_id                  BIGINT NOT NULL,
    station_id                  BIGINT NOT NULL,
    connector_id                INT NOT NULL,
    reservation_start           TIMESTAMP NOT NULL,
    reservation_end             TIMESTAMP NOT NULL,
    planned_energy_kwh          NUMERIC(10,2) NOT NULL,
    priority                    INT NOT NULL DEFAULT 0,
    charging_strategy           VARCHAR(100) NOT NULL, -- Immediate, OffPeak, RenewablePreferred, CheapestAvailable, BalancedFleet
    status                      VARCHAR(50) NOT NULL -- BOOKED, ACTIVE, COMPLETED, CANCELLED
);
