-- V273: Machinery Telemetry
CREATE TABLE IF NOT EXISTS platform_machinery_telemetry (
    id                  BIGSERIAL PRIMARY KEY,
    device_id           BIGINT NOT NULL,
    signal_id           BIGINT NOT NULL,
    recorded_at         TIMESTAMP NOT NULL,
    quality             VARCHAR(50) NOT NULL, -- GOOD, BAD, UNCERTAIN
    value               NUMERIC(19,4) NOT NULL,
    unit                VARCHAR(50) NOT NULL,
    sequence_num        BIGINT NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_machinery_telemetry_time ON platform_machinery_telemetry (recorded_at DESC);
