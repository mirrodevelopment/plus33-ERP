-- V283: Route Deviancies
CREATE TABLE IF NOT EXISTS platform_route_deviancy_log (
    id                  BIGSERIAL PRIMARY KEY,
    transit_route_id    BIGINT NOT NULL,
    expected_route_wkt  TEXT NOT NULL,
    actual_route_wkt    TEXT NOT NULL,
    deviation_distance  NUMERIC(10,2) NOT NULL,
    deviation_duration_minutes INT NOT NULL,
    severity            VARCHAR(50) NOT NULL, -- Low, Medium, High
    automatic_recovery  BOOLEAN NOT NULL DEFAULT FALSE,
    reroute_triggered   BOOLEAN NOT NULL DEFAULT FALSE,
    detected_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
