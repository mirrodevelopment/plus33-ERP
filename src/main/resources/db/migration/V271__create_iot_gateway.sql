-- V271: IoT Gateway Registry & Heartbeats
CREATE TABLE IF NOT EXISTS platform_iot_gateway (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    gateway_code        VARCHAR(100) NOT NULL UNIQUE,
    gateway_status      VARCHAR(50) NOT NULL DEFAULT 'OFFLINE',
    firmware_version    VARCHAR(50) NOT NULL,
    certificate_thumbprint VARCHAR(150) NOT NULL,
    edge_cluster        VARCHAR(100) NOT NULL,
    mqtt_client_id      VARCHAR(100) NOT NULL UNIQUE,
    last_seen           TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_gateway_heartbeat (
    id                  BIGSERIAL PRIMARY KEY,
    gateway_id          BIGINT NOT NULL,
    recorded_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
