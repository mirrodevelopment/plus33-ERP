-- V281: Geofence Definitions
CREATE TABLE IF NOT EXISTS platform_geofence_definition (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    geofence_code       VARCHAR(100) NOT NULL UNIQUE,
    geofence_type       VARCHAR(50) NOT NULL, -- Polygon, Circle, Rectangle, Corridor
    geometry_wkt        TEXT NOT NULL,
    center_lat          NUMERIC(9,6) NOT NULL,
    center_lng          NUMERIC(9,6) NOT NULL,
    radius_meters       NUMERIC(10,2),
    altitude_min        NUMERIC(7,2),
    altitude_max        NUMERIC(7,2),
    timezone            VARCHAR(100) NOT NULL,
    active_from         TIMESTAMP,
    active_until        TIMESTAMP,
    tenant_id           VARCHAR(100) NOT NULL DEFAULT 'DEFAULT_TENANT'
);
