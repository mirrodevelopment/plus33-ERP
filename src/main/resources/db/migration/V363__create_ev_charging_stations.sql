-- V363: Charging Stations Registry
CREATE TABLE IF NOT EXISTS platform_ev_charging_station (
    id                          BIGSERIAL PRIMARY KEY,
    station_code                VARCHAR(100) NOT NULL UNIQUE,
    operator                    VARCHAR(100) NOT NULL,
    location_name               VARCHAR(200) NOT NULL,
    latitude                    NUMERIC(10,6) NOT NULL,
    longitude                   NUMERIC(10,6) NOT NULL,
    charger_type                VARCHAR(50) NOT NULL, -- AC, DC
    connector_type              VARCHAR(50) NOT NULL, -- CCS2, CHAdeMO, Type2, NACS
    max_power_kw                NUMERIC(7,2) NOT NULL,
    simultaneous_connectors     INT NOT NULL,
    availability_status         VARCHAR(50) NOT NULL, -- AVAILABLE, OCCUPIED, OFFLINE
    tariff_plan_code            VARCHAR(100) NOT NULL,
    renewable_supported         BOOLEAN NOT NULL DEFAULT FALSE
);
