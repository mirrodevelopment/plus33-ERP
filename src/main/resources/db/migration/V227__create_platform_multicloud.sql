-- V227: Multi-Cloud DDL
CREATE TABLE IF NOT EXISTS platform_multicloud_sync_profile (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    provider_name       VARCHAR(100) NOT NULL UNIQUE, -- AWS, AZURE, GCP
    target_endpoint     VARCHAR(250) NOT NULL,
    active              BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS platform_multicloud_sync_history (
    id                  BIGSERIAL PRIMARY KEY,
    provider_name       VARCHAR(100) NOT NULL,
    records_synced      INT NOT NULL,
    latency_ms          BIGINT NOT NULL,
    sync_status         VARCHAR(50) NOT NULL DEFAULT 'SUCCESS',
    timestamp           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
