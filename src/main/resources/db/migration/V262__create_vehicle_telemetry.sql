-- V262: Vehicle Telemetry
CREATE TABLE IF NOT EXISTS platform_vehicle (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    vehicle_code        VARCHAR(100) NOT NULL UNIQUE,
    capacity_kg         NUMERIC(10,2) NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE'
);

CREATE TABLE IF NOT EXISTS platform_vehicle_telemetry (
    id                  BIGSERIAL PRIMARY KEY,
    vehicle_id          BIGINT NOT NULL,
    latitude            NUMERIC(9,6) NOT NULL,
    longitude           NUMERIC(9,6) NOT NULL,
    speed_kmh           NUMERIC(5,2) NOT NULL,
    recorded_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_transit_route (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    vehicle_id          BIGINT NOT NULL,
    origin_node_id      BIGINT NOT NULL,
    dest_node_id        BIGINT NOT NULL,
    route_path_json     TEXT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'IN_TRANSIT'
);
