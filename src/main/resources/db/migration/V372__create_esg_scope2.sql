-- V372: Scope 2 Indirect Emissions
CREATE TABLE IF NOT EXISTS platform_esg_scope2_log (
    id                          BIGSERIAL PRIMARY KEY,
    vehicle_id                  BIGINT NOT NULL,
    charging_station_id         BIGINT NOT NULL,
    energy_kwh                  NUMERIC(10,2) NOT NULL,
    grid_region                 VARCHAR(100) NOT NULL,
    grid_factor                 NUMERIC(10,4) NOT NULL,
    renewable_percentage        NUMERIC(5,2) NOT NULL,
    market_based_co2e_kg        NUMERIC(10,2) NOT NULL,
    location_based_co2e_kg      NUMERIC(10,2) NOT NULL,
    logged_at                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
