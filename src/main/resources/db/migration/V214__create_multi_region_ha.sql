-- V214: Multi-Region HA DDL
CREATE TABLE IF NOT EXISTS platform_region_profile (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    region_code         VARCHAR(50) NOT NULL UNIQUE,
    health_score        INT NOT NULL DEFAULT 100, -- CPU, Memory, network, etc combined score (0-100)
    cpu_utilization     NUMERIC(5,2) NOT NULL DEFAULT 0.00,
    memory_utilization  NUMERIC(5,2) NOT NULL DEFAULT 0.00,
    network_rtt_ms      INT NOT NULL DEFAULT 5,
    failed_queries      INT NOT NULL DEFAULT 0,
    disk_utilization    NUMERIC(5,2) NOT NULL DEFAULT 0.00,
    active              BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS platform_replication_lag_log (
    id                  BIGSERIAL PRIMARY KEY,
    region_code         VARCHAR(50) NOT NULL,
    lag_ms              BIGINT NOT NULL,
    recorded_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
