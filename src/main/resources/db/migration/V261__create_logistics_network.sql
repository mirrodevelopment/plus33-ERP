-- V261: Logistics Network
CREATE TABLE IF NOT EXISTS platform_logistics_node (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    node_code           VARCHAR(100) NOT NULL UNIQUE,
    node_type           VARCHAR(100) NOT NULL, -- warehouse/store/factory/port/customer
    latitude            NUMERIC(9,6) NOT NULL,
    longitude           NUMERIC(9,6) NOT NULL,
    geohash             VARCHAR(50),
    region              VARCHAR(100) NOT NULL,
    timezone            VARCHAR(100) NOT NULL,
    capacity            INT NOT NULL DEFAULT 0,
    status              VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    metadata_json       TEXT
);

CREATE TABLE IF NOT EXISTS platform_shipping_lane (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    source_node_id      BIGINT NOT NULL,
    destination_node_id BIGINT NOT NULL,
    distance_km         NUMERIC(10,2) NOT NULL,
    expected_duration_minutes INT NOT NULL,
    transport_mode      VARCHAR(50) NOT NULL, -- truck/train/ship/air
    cost_factor         NUMERIC(5,2) NOT NULL DEFAULT 1.00,
    carbon_factor       NUMERIC(5,2) NOT NULL DEFAULT 1.00,
    status              VARCHAR(50) NOT NULL DEFAULT 'ACTIVE'
);
