-- V228: Cloud Resource Inventory DDL
CREATE TABLE IF NOT EXISTS platform_cloud_resource (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    resource_id         VARCHAR(250) NOT NULL UNIQUE,
    resource_type       VARCHAR(100) NOT NULL, -- VM, DB, REDIS, KAFKA, etc
    provider            VARCHAR(50) NOT NULL, -- AWS, AZURE, GCP
    region              VARCHAR(100) NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'RUNNING',
    cost_daily          NUMERIC(19,4) NOT NULL DEFAULT 0.00,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
