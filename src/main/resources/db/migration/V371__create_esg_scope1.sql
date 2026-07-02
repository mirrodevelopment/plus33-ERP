-- V371: Scope 1 Direct Emissions
CREATE TABLE IF NOT EXISTS platform_esg_scope1_log (
    id                          BIGSERIAL PRIMARY KEY,
    vehicle_id                  BIGINT NOT NULL,
    fuel_type                   VARCHAR(50) NOT NULL, -- Diesel, Petrol, LPG, CNG, Biodiesel, HVO
    fuel_consumed_liters        NUMERIC(10,2) NOT NULL,
    co2e_kg                     NUMERIC(10,2) NOT NULL,
    ch4_kg                      NUMERIC(10,3) NOT NULL,
    n2o_kg                      NUMERIC(10,3) NOT NULL,
    emission_factor             NUMERIC(10,4) NOT NULL,
    calculation_method          VARCHAR(100) NOT NULL,
    trip_id                     BIGINT NOT NULL,
    logged_at                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
